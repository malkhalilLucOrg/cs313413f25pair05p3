package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {
  // TODO entirely your job (except onCircle)
  private final Canvas canvas;
  private final Paint paint;
  public Draw(final Canvas canvas, final Paint paint) {
    this.canvas = canvas; // FIXME
    this.paint = paint; // FIXME
    paint.setStyle(Style.STROKE);
  }
  @Override
  public Void onCircle(final Circle c) {
    canvas.drawCircle(0, 0, c.getRadius(), paint);
    return null;
  }

  @Override
  public Void onStrokeColor(final StrokeColor c) {
    int oldColor = paint.getColor();
    paint.setColor(c.getColor());
    c.getShape().accept(this);
    paint.setColor(oldColor);
    return null;
  }

  @Override
  public Void onFill(final Fill f) {
    Style oldStyle = paint.getStyle();
    paint.setStyle(Style.FILL_AND_STROKE);
    f.getShape().accept(this);
    paint.setStyle(oldStyle);
    return null;
  }

  @Override
  public Void onGroup(final Group g) {
    for (Shape s : g.getShapes()) {
      s.accept(this);
    }
    return null;
  }

  @Override
  public Void onLocation(final Location l) {
    canvas.translate(l.getX(), l.getY());
    l.getShape().accept(this);
    canvas.translate(-l.getX(), -l.getY());
    return null;
  }

  @Override
  public Void onRectangle(final Rectangle r) {
    canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
    return null;
  }

  @Override
  public Void onOutline(Outline o) {
    Style oldStyle = paint.getStyle();
    paint.setStyle(Style.STROKE);
    o.getShape().accept(this);
    paint.setStyle(oldStyle);
    return null;
  }

  @Override
  public Void onPolygon(final Polygon s) {
    var points = s.getPoints();
    //      if (points.size() < 2) return null;
    float[] pts = new float[(points.size() - 1) * 4];
    for (int i = 0; i < points.size() - 1; i++) {
      pts[i * 4] = points.get(i).getX();
      pts[i * 4 + 1] = points.get(i).getY();
      pts[i * 4 + 2] = points.get(i + 1).getX();
      pts[i * 4 + 3] = points.get(i + 1).getY();
    }
    canvas.drawLines(pts, paint);
    // Draw closing line
    canvas.drawLine(
      points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY(),
      points.get(0).getX(), points.get(0).getY(),
      paint
    );
    return null;
  }
}
