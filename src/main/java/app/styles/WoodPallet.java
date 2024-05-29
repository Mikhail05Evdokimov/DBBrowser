package app.styles;

import io.qt.core.Qt;
import io.qt.gui.*;

import static io.qt.core.Qt.GlobalColor.darkGreen;
import static io.qt.core.Qt.GlobalColor.white;
import static io.qt.gui.QImage.Format.Format_RGB32;
import static io.qt.gui.QPalette.ColorGroup.NColorGroups;

public class WoodPallet extends QPalette {

    public QPainter painter = new QPainter();

    public WoodPallet() {
        QColor brown = new QColor(212, 140, 95);
        QColor beige = new QColor(236, 182, 120);
        QColor slightlyOpaqueBlack = new QColor(0, 0, 0, 63);

        QImage backgroundImage = new QImage("../woodbackground.png");
        QImage buttonImage = new QImage("../woodbackground.png");
        QImage midImage = buttonImage.convertToFormat(Format_RGB32);

        painter.begin(midImage);
        painter.setPen(Qt.PenStyle.NoPen);
        painter.fillRect(midImage.rect(), slightlyOpaqueBlack);
        painter.end();

        setColor(ColorRole.Button, brown);

        setBrush(ColorRole.BrightText, new QBrush(white));
        setBrush(ColorRole.Base, new QBrush(beige));
        setBrush(ColorRole.Highlight, new QBrush(darkGreen));

        setTexture(ColorRole.Button, buttonImage);
        setTexture(ColorRole.Mid, midImage);
        setTexture(ColorRole.Window, backgroundImage);

        QBrush brush = this.window();
        brush.setColor(brush.color().darker());

        setBrush(ColorGroup.Disabled, ColorRole.WindowText, brush);
        setBrush(ColorGroup.Disabled, ColorRole.Text, brush);
        setBrush(ColorGroup.Disabled, ColorRole.ButtonText, brush);
        setBrush(ColorGroup.Disabled, ColorRole.Base, brush);
        setBrush(ColorGroup.Disabled, ColorRole.Button, brush);
        setBrush(ColorGroup.Disabled, ColorRole.Mid, brush);

    }

    private void setTexture(QPalette.ColorRole role, QImage image)
    {
        for (int i = 0; i < NColorGroups.value(); ++i) {
            QBrush brush = new QBrush(image);
            brush.setColor(this.brush(QPalette.ColorGroup.resolve(i), role).color());
            this.setBrush(QPalette.ColorGroup.resolve(i), role, brush);
        }
    }

}
