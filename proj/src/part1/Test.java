package part1;

import java.util.UUID;

public class Test implements HasUUID{

    private MyUUID m_uuid;

    public Test(){
        this.m_uuid=new MyUUID(getClass().getSimpleName());

    }

    @Override
    public UUID getUUID() {
        return this.m_uuid.GetUUID();
    }

    public static void main(String[] args) {
        Test test=new Test();
       System.out.println(test.getUUID());
       System.out.println(MyUUID.Encoder("Test"));
    }
}
