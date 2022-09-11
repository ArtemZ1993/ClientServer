package part1;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

// MyUUID final class to prevent inheritance
public final class MyUUID implements Comparable<MyUUID> {

    private final String m_Key;
    private final UUID m_UUID;

    public MyUUID(@NotNull String i_Key){

        m_Key = i_Key;
        m_UUID = Encoder(i_Key);//Encoder is a static function that convert String key to UUID
    }

    //Encoder: String->UUID
    public static UUID Encoder(@NotNull final String i_Key){

        byte[] byteArray = i_Key.getBytes();
        UUID generatedUUID = UUID.nameUUIDFromBytes(byteArray);

        return generatedUUID;
    }
    //Decoder: UUID->String
    public static  String Decoder(@NotNull final MyUUID i_MyUUID){

        String uuidString = i_MyUUID.GetStringUUID();

        return uuidString;
    }

    public String GetStringUUID(){
        return m_Key;
    }

    public UUID GetUUID() {
        return m_UUID;
    }
    
    @Override
    public String toString() {
        return String.format("MyUUID %s", m_Key);
    }

    @Override
    public int compareTo(@NotNull MyUUID o) { return m_UUID.compareTo(o.GetUUID()); }

}
