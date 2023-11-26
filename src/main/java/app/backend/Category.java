package app.backend;

public class Category {
    // Поля класса
    public int category_id;
    public String category_name;
    public String category_weight;




    // Конструктор
    public Category(int category_id, String category_name, String category_weight) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_weight = category_weight;
    }

    // Выводим информацию по продукту
    @Override
    public String toString() {
        return String.format("ID: %s | name: %s | weight: %s",
            this.category_id, this.category_name, this.category_weight);
    }
}