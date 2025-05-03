package com.turtle;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import uk.ac.leedsbeckett.oop.LBUGraphics;

public class TurtleGraphicsApp extends LBUGraphics {
    public static void main(String[] args) {
        new TurtleGraphicsApp();
    }

    public TurtleGraphicsApp() {
        JFrame mainFrame = new JFrame("Turtle Graphics Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(this);
        mainFrame.pack();
        mainFrame.setVisible(true);

        about();
        reset();
        setPenColour(Color.RED);
        setStroke(2);
        setPenState(true);
        displayMessage("Welcome to Turtle Graphics! Type 'help' for commands.");
    }

    @Override
    public void processCommand(String command) {
        try {
            String[] parts = command.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) return;

            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "forward":
                case "fd":
                    if (parts.length == 2) {
                        forward(Integer.parseInt(parts[1]));
                    } else {
                        displayMessage("Syntax: forward <distance>");
                    }
                    break;

                case "backward":
                case "bk":
                    if (parts.length == 2) {
                        forward(-Integer.parseInt(parts[1]));
                    } else {
                        displayMessage("Syntax: backward <distance>");
                    }
                    break;

                case "right":
                case "rt":
                    if (parts.length == 2) {
                        right(Integer.parseInt(parts[1]));
                    } else {
                        right(90);
                    }
                    break;

                case "left":
                case "lt":
                    if (parts.length == 2) {
                        left(Integer.parseInt(parts[1]));
                    } else {
                        left(90);
                    }
                    break;

                case "penup":
                case "pu":
                    setPenState(false);
                    displayMessage("Pen lifted");
                    break;

                case "pendown":
                case "pd":
                    setPenState(true);
                    displayMessage("Pen lowered");
                    break;

                case "setpencolor":
                    if (parts.length == 2) {
                        setPenColor(parts[1]);
                    } else {
                        displayMessage("Syntax: setpencolor <color>");
                    }
                    break;

                case "clear":
                    clear();
                    displayMessage("Canvas cleared");
                    break;

                case "reset":
                    reset();
                    displayMessage("Turtle reset");
                    break;

                case "circle":
                    if (parts.length == 2) {
                        circle(Integer.parseInt(parts[1]));
                    } else {
                        displayMessage("Syntax: circle <radius>");
                    }
                    break;

                case "square":
                    if (parts.length == 2) {
                        drawSquare(Integer.parseInt(parts[1]));
                    } else {
                        displayMessage("Syntax: square <size>");
                    }
                    break;

                case "polygon":
                    if (parts.length == 3) {
                        drawPolygon(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    } else {
                        displayMessage("Syntax: polygon <sides> <size>");
                    }
                    break;

                case "save":
                    if (parts.length == 2) {
                        saveDrawing(parts[1]);
                    } else {
                        displayMessage("Syntax: save <filename>");
                    }
                    break;

                case "load":
                    if (parts.length == 2) {
                        loadDrawing(parts[1]);
                    } else {
                        displayMessage("Syntax: load <filename>");
                    }
                    break;

                case "setspeed":
                    if (parts.length == 2) {
                        setTurtleSpeed(Integer.parseInt(parts[1]));
                        displayMessage("Speed set to " + parts[1]);
                    } else {
                        displayMessage("Syntax: setspeed <1-10>");
                    }
                    break;

                case "help":
                    showHelp();
                    break;

                default:
                    displayMessage("Unknown command: " + cmd);
            }
        } catch (NumberFormatException e) {
            displayMessage("Please enter a valid number");
        } catch (ArrayIndexOutOfBoundsException e) {
            displayMessage("Missing parameter for command");
        } catch (Exception e) {
            displayMessage("Error: " + e.getMessage());
        }
    }

    private void setPenColor(String colorStr) {
        try {
            Color color;
            if (colorStr.startsWith("#") && colorStr.length() == 7) {
                color = Color.decode(colorStr);
            } else {
                switch (colorStr.toLowerCase()) {
                    case "red": color = Color.RED; break;
                    case "green": color = Color.GREEN; break;
                    case "blue": color = Color.BLUE; break;
                    case "black": color = Color.BLACK; break;
                    case "white": color = Color.WHITE; break;
                    case "yellow": color = Color.YELLOW; break;
                    case "cyan": color = Color.CYAN; break;
                    case "magenta": color = Color.MAGENTA; break;
                    default: throw new IllegalArgumentException("Unknown color");
                }
            }
            setPenColour(color);
            displayMessage("Pen color set to " + colorStr);
        } catch (Exception e) {
            displayMessage("Invalid color: " + colorStr + ". Try: red, green, blue, etc. or #RRGGBB");
        }
    }

    private void drawSquare(int size) {
        setPenState(true);
        for (int i = 0; i < 4; i++) {
            forward(size);
            right(90);
        }
    }

    private void drawPolygon(int sides, int size) {
        if (sides < 3) {
            displayMessage("Polygon must have at least 3 sides");
            return;
        }
        setPenState(true);
        int angle = 360 / sides;
        for (int i = 0; i < sides; i++) {
            forward(size);
            right(angle);
        }
    }

    private void saveDrawing(String filename) {
        try {
            BufferedImage image = getBufferedImage();
            if (!filename.toLowerCase().endsWith(".png")) {
                filename += ".png";
            }
            ImageIO.write(image, "PNG", new File(filename));
            displayMessage("Drawing saved as " + filename);
        } catch (Exception e) {
            displayMessage("Error saving: " + e.getMessage());
        }
    }

    private void loadDrawing(String filename) {
        try {
            if (!filename.toLowerCase().endsWith(".png")) {
                filename += ".png";
            }
            BufferedImage image = ImageIO.read(new File(filename));
            setBufferedImage(image);
            displayMessage("Drawing loaded from " + filename);
        } catch (Exception e) {
            displayMessage("Error loading: " + e.getMessage());
        }
    }

    private void showHelp() {
        StringBuilder helpText = new StringBuilder();
        helpText.append("=== Turtle Graphics Commands ===\n");
        helpText.append("Movement:\n");
        helpText.append("  forward <n> or fd <n> - Move forward n pixels\n");
        helpText.append("  backward <n> or bk <n> - Move backward n pixels\n");
        helpText.append("  right <n> or rt <n> - Turn right n degrees\n");
        helpText.append("  left <n> or lt <n> - Turn left n degrees\n");
        helpText.append("\nDrawing Control:\n");
        helpText.append("  penup or pu - Lift pen (stop drawing)\n");
        helpText.append("  pendown or pd - Lower pen (start drawing)\n");
        helpText.append("  setpencolor <color> - Set pen color\n");
        helpText.append("  clear - Clear the canvas\n");
        helpText.append("  reset - Reset turtle position\n");
        helpText.append("\nShapes:\n");
        helpText.append("  circle <r> - Draw circle with radius r\n");
        helpText.append("  square <size> - Draw square\n");
        helpText.append("  polygon <sides> <size> - Draw regular polygon\n");
        helpText.append("\nFile Operations:\n");
        helpText.append("  save <file> - Save drawing as PNG\n");
        helpText.append("  load <file> - Load drawing from PNG\n");
        helpText.append("\nSettings:\n");
        helpText.append("  setspeed <1-10> - Set animation speed\n");
        helpText.append("  help - Show this help");

        displayMessage(helpText.toString());
    }
}
