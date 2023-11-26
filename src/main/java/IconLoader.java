import io.qt.gui.QIcon;
import io.qt.gui.QPixmap;

import java.io.IOException;
import java.util.Objects;

public class IconLoader {

    /**
     * Хз почему так, но теперь иконки загружаем через этот класс.
     * Можно подумать над параметризацией конструктора, или
     * захардкодить методы, которые возвращают разные иконки для разных нужд.
     */
    public QIcon loadIcon() throws IOException {
        QPixmap pixmap = new QPixmap();
        pixmap.loadFromData(Objects.requireNonNull(this.getClass().getResourceAsStream("looool.png")).readAllBytes());
        QIcon icon = new QIcon();
        icon.addPixmap(pixmap);
        return icon;
    }

}
