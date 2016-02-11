package hashcode;

import io.shimmen.simpleascii.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        AsciiReader reader = new AsciiReader("test.in");



        System.out.print(reader.nextLine().getSectionAtIndex(0));

        AsciiWriter writer = new AsciiWriter(".out");
    }
}
