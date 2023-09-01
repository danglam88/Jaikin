package com.jaikin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ChaikinsAlgorithm extends Application {

    private Canvas canvas;

    private GraphicsContext gc;

    private Timeline timeline;

    private List<Point> originalPoints;

    private List<Point> points = new ArrayList<>();

    private Point draggedPoint;

    private boolean dragging = false;

    private boolean running = false;

    private boolean showMessage = false;

    private int step = 0;

    private static class Point {

        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double getX() {
            return this.x;
        }

        double getY() {
            return this.y;
        }

        void setX(double x) {
            this.x = x;
        }

        void setY(double y) {
            this.y = y;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chaikins Algorithm Animation");
        BorderPane root = new BorderPane();

        canvas = new Canvas(1000, 1000);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        canvas.setOnMouseClicked(event -> {
            if (!running) {
                if (showMessage) {
                    showMessage = false;
                    clearCanvas();
                }

                for (Point point: points) {
                    double distance = Math.sqrt(Math.pow(event.getX() - point.getX(), 2) + Math.pow(event.getY() - point.getY(), 2));
                    if (distance <= 5) {
                        dragging = true;
                        draggedPoint = point;
                        break;
                    }
                }

                if (!dragging && draggedPoint == null) {
                    points.add(new Point(event.getX(), event.getY()));
                    drawPoint(event.getX(), event.getY());
                }
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (!running && dragging && draggedPoint != null) {
                clearCanvas();
                draggedPoint.setX(event.getX());
                draggedPoint.setY(event.getY());
                drawPoints();
            }
        });

        canvas.setOnMouseReleased(event -> {
            if (!running) {
                dragging = false;
                draggedPoint = null;
            }
        });

        root.setCenter(canvas);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !running) {
                if (points.isEmpty() && !showMessage) {
                    showMessage = true;
                    gc.setFill(Color.WHITE);
                    gc.setFont(new Font(20));
                    gc.fillText("Please add at least one point to this black board\n" +
                            "(This message will disappear when you add a point)", 10, 30);
                } else if (!showMessage) {
                    running = true;
                    if (points.size() == 2) {
                        drawLine(points.get(0), points.get(1));
                    } else if (points.size() > 2) {
                        originalPoints = new ArrayList<>(points);
                        startAnimation();
                    }
                }
            } else if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.SHIFT) {
                running = false;
                showMessage = false;
                if (timeline != null) {
                    timeline.stop();
                }
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                } else if (event.getCode() == KeyCode.SHIFT) {
                    clearCanvas();
                    points = new ArrayList<>();
                    originalPoints = null;
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawPoint(double x, double y) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1.0);
        gc.strokeOval(x - 5, y - 5, 10, 10);
    }

    private void drawPoints() {
        for (Point point: originalPoints) {
            drawPoint(point.getX(), point.getY());
        }
    }

    private void drawLine(Point p1, Point p2) {
        gc.setStroke(Color.WHITE);
        gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    private void drawLines() {
        Point firstPoint = originalPoints.get(0);
        Point lastPoint = originalPoints.get(originalPoints.size() - 1);
        drawLine(firstPoint, points.get(0));
        drawLine(lastPoint, points.get(points.size() - 1));
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            drawLine(p1, p2);
        }
    }

    private void clearCanvas() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void startAnimation() {
        timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
            if (step < 7) {
                clearCanvas();
                drawPoints();
                drawLines();

                List<Point> newPoints = new ArrayList<>();
                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i + 1);
                    double x1 = 0.75 * p1.getX() + 0.25 * p2.getX();
                    double y1 = 0.75 * p1.getY() + 0.25 * p2.getY();
                    double x2 = 0.25 * p1.getX() + 0.75 * p2.getX();
                    double y2 = 0.25 * p1.getY() + 0.75 * p2.getY();
                    newPoints.add(new Point(x1, y1));
                    newPoints.add(new Point(x2, y2));
                }

                points = newPoints;
                step++;
            } else {
                step = 0;
                points = new ArrayList<>(originalPoints);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
