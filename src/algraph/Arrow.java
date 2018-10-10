package algraph;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import java.awt.geom.Point2D;

/**
 * An object to draw an arrow
 */
class Arrow extends Group {
	private Line _body;
	private Polygon _head;

	public Arrow() {
		super();
		_body = new Line();
		_head = new Polygon(0, 0, 0, 0, 0, 0);
		this.getChildren().addAll(_body, _head);
	}

	public double getLength() {
		return Math.sqrt(
			Math.pow(_body.getStartX() - _body.getEndX(), 2) +
			Math.pow(_body.getStartY() - _body.getEndY(), 2));
	}

	private Point2D.Double calculateHeadPosition() {
		Point2D.Double pos = new Point2D.Double(getEndX() - getStartX(), getEndY() - getStartY());
		pos.x *= (getLength() - Node.RADIUS) / getLength();
		pos.y *= (getLength() - Node.RADIUS) / getLength();
		pos.x += getStartX();
		pos.y += getStartY();
		return pos;
	}

	private void updateHead() {
		final double headLength = 10;
		final double headAngle = Math.toRadians(20);
		double angle = Math.atan2(getEndY() - getStartY(), getEndX() - getStartX());

		Point2D.Double headPos = calculateHeadPosition();
		Point2D.Double corner1 = new Point2D.Double(
			headPos.x - headLength * Math.cos(angle + headAngle),
			headPos.y - headLength * Math.sin(angle + headAngle));
		Point2D.Double corner2 = new Point2D.Double(
			headPos.x - headLength * Math.cos(angle - headAngle),
			headPos.y - headLength * Math.sin(angle - headAngle));

		_head.getPoints().setAll(new Double[]{
			headPos.x, headPos.y,
			corner1.x, corner1.y, 
			corner2.x, corner2.y});
	}

	public void setLineStart(double x, double y) {
		_body.setStartX(x);
		_body.setStartY(y);
		updateHead();
	}

	public void setLineEnd(double x, double y) {
		_body.setEndX(x);
		_body.setEndY(y);
		updateHead();
	}

	public double getStartX() {
		return _body.getStartX();
	}
	public double getStartY() {
		return _body.getStartY();
	}
	public double getEndX() {
		return _body.getEndX();
	}
	public double getEndY() {
		return _body.getEndY();
	}
}
