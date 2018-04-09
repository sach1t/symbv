package pathanalysis;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChangeTag implements Tag {
    String type;

    public ChangeTag(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return "symbvTag";
    }

    @Override
    public byte[] getValue() throws AttributeValueException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeChars(type);
            dos.flush();
        } catch(IOException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }

    @Override
    public String toString() {
        return this.getName() + ": " + type;
    }
}
