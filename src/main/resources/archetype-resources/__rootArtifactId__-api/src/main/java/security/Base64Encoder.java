package ${package}.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author VEDRAX SAS
 */
public final class Base64Encoder {

    private static final int BUFFER_SIZE = 1024;
    private static final byte encoding[] = {
        (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D',
        (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', // 0-7
        (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
        (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', // 8-15
        (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
        (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', // 16-23
        (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b',
        (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', // 24-31
        (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j',
        (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', // 32-39
        (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r',
        (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', // 40-47
        (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
        (byte) '0', (byte) '1', (byte) '2', (byte) '3', // 48-55
        (byte) '4', (byte) '5', (byte) '6', (byte) '7',
        (byte) '8', (byte) '9', (byte) '+', (byte) '/', // 56-63
        (byte) '=' // 64
    };

    /**
     * Encodes data from supplied input to output.
     *
     * @param in The input stream to be encoded.
     * @param out The output stream, to write encoded data to.
     * @throws java.io.IOException
     */
    public static void encode(InputStream in, OutputStream out) throws IOException {
        process(in, out);
    }

    /**
     * Encode the supplied byte array and write the encoded data to the
     * OutputStream <i>out</i>.
     * @param input
     * @param out
     * @throws java.io.IOException
     */
    public static void encode(byte input[], OutputStream out) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        process(in, out);
    }

    /**
     * Encode the given string,and return the encoded version as a string.
     *
     * @param input
     * @return A String, representing the encoded content of the input String.
     * @throws java.io.IOException
     */
    public static String encode(String input) throws IOException {
        byte bytes[];
        bytes = input.getBytes("ISO-8859-1");
        return encode(bytes);
    }

    /**
     * Encode the given byte array and return the result as a string.
     * @param bytes
     * @return 
     * @throws java.io.IOException
     */
    public static String encode(byte bytes[]) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        process(in, out);
        return out.toString("ISO-8859-1");
    }

// Private ----------------------------------------------------------------
    private static int get1(byte buf[], int off) {
        return (buf[off] & 0xfc) >> 2;
    }

    private static int get2(byte buf[], int off) {
        return ((buf[off] & 0x3) << 4) | ((buf[off + 1] & 0xf0) >>> 4);
    }

    private static int get3(byte buf[], int off) {
        return ((buf[off + 1] & 0x0f) << 2) | ((buf[off + 2] & 0xc0) >>> 6);
    }

    private static int get4(byte buf[], int off) {
        return buf[off + 2] & 0x3f;
    }

    /**
     * Process the data: encode the input stream to the output stream. This
     * method runs through the input stream, encoding it to the output stream.
     *
     * @exception IOException If we weren't able to access the input stream or
     * the output stream.
     */
    private static void process(InputStream in, OutputStream out) throws IOException {
        byte buffer[] = new byte[BUFFER_SIZE];
        int got = -1;
        int off = 0;
        int count = 0;
        while ((got = in.read(buffer, off, BUFFER_SIZE - off)) > 0) {
            if (got >= 3) {
                got += off;
                off = 0;
                while (off + 3 <= got) {
                    int c1 = get1(buffer, off);
                    int c2 = get2(buffer, off);
                    int c3 = get3(buffer, off);
                    int c4 = get4(buffer, off);
                    switch (count) {
                        case 73:
                            out.write(encoding[c1]);
                            out.write(encoding[c2]);
                            out.write(encoding[c3]);
                            out.write('\n');
                            out.write(encoding[c4]);
                            count = 1;
                            break;
                        case 74:
                            out.write(encoding[c1]);
                            out.write(encoding[c2]);
                            out.write('\n');
                            out.write(encoding[c3]);
                            out.write(encoding[c4]);
                            count = 2;
                            break;
                        case 75:
                            out.write(encoding[c1]);
                            out.write('\n');
                            out.write(encoding[c2]);
                            out.write(encoding[c3]);
                            out.write(encoding[c4]);
                            count = 3;
                            break;
                        case 76:
                            out.write('\n');
                            out.write(encoding[c1]);
                            out.write(encoding[c2]);
                            out.write(encoding[c3]);
                            out.write(encoding[c4]);
                            count = 4;
                            break;
                        default:
                            out.write(encoding[c1]);
                            out.write(encoding[c2]);
                            out.write(encoding[c3]);
                            out.write(encoding[c4]);
                            count += 4;
                            break;
                    }
                    off += 3;
                }
                // Copy remaining bytes to beginning of buffer:
                for (int i = 0; i < 3; i++) {
                    buffer[i] = (i < got - off) ? buffer[off + i] : ((byte) 0);
                }
                off = got - off;
            } else {
                // Total read amount is less then 3 bytes:
                off += got;
            }
        }
        // Manage the last bytes, from 0 to off:
        switch (off) {
            case 1:
                out.write(encoding[get1(buffer, 0)]);
                out.write(encoding[get2(buffer, 0)]);
                out.write('=');
                out.write('=');
                break;
            case 2:
                out.write(encoding[get1(buffer, 0)]);
                out.write(encoding[get2(buffer, 0)]);
                out.write(encoding[get3(buffer, 0)]);
                out.write('=');
        }
        return;
    }
}
