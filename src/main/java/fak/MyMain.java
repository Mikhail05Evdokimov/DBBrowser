package fak;

public class MyMain {

    public void doIt(){
        MyCoolReceiver receiver = new MyCoolReceiver() {
            @Override
            public void updateContent() {
                System.out.println("update");
            }
        };

        receiver.createCool();
    }

}
