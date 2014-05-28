package tech.magnitude.abson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class BsonUtil {

	public static byte[] toBinaryCString(String source) {
		byte[] bytes = new byte[0];
		try {
			bytes = source.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] res = new byte[bytes.length + 1];
		System.arraycopy(bytes, 0, res, 0, bytes.length);
		res[res.length - 1] = 0x0;
		return res;
	}
	
	public static byte[] toBinaryString(String source) {
		byte[] bytes = toBinaryCString(source);
		byte[] res = new byte[bytes.length + 4];
		System.arraycopy(bytes, 0, res, 4, bytes.length);
		System.arraycopy(toBinaryInt32(bytes.length), 0, res, 0, 4);
		return res;
	}
	
	public static byte[] toBinaryEString(String source) {
		//alias for cstring
		return toBinaryCString(source);
	}
	
	
	
	/* ************** *
	 * * Primitives * *
	 * ************** */
	
	public static byte[] toBinaryInt32(int source) {
		byte[] res = new byte[4];
		for (int i=0; i<4; i++) {
			res[i] = (byte)(source & 0xff);
			source >>= 8;
		}
		return res;
	}
	
	public static int fromBinaryInt32(byte[] bytes) {
		int res = 0;
		for (int i=0; i<4; i++) {
			res |= bytes[i];
			res <<= 4;
		}
		return res;
	}
	
	public static byte[] toBinaryInt64(long source) {
		byte[] res = new byte[8];
		for (int i=0; i<8; i++) {
			res[i] = (byte)(source & 0xff);
			source >>= 8;
		}
		return res;
	}
	
	public static long fromBinaryInt64(byte[] bytes) {
		int res = 0;
		for (int i=0; i<8; i++) {
			res |= bytes[i];
			res <<= 4;
		}
		return res;
	}
	
	public static byte[] toBinaryBoolean(boolean source) {
		return new byte[] {(byte)(source ? 0x01 : 0x00)};
	}
	
	public static byte[] toBinaryFloatingPoint(double source) {
		byte[] res = new byte[8];
		ByteBuffer.wrap(res).putDouble(source);
		return res;
	}
	
	public static double fromBinaryFloatingPoint(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getDouble();
	}
	
	public static String byteString(ByteArrayOutputStream stream) {
		return byteString(stream.toByteArray());
	}
	
	public static String byteString(byte[] bytes) {
		StringBuilder res = new StringBuilder();
		for (byte cur : bytes) {
			res.append(String.format("\\x%02x", cur));
		}
		return res.toString();
	}
	
	public static String readCString(InputStream stream) throws IOException {
		String res = "";
		while (true) {
			int c = stream.read();
			if (c == 0x00) break;
			res += (char)c;
		}
		return res;
	}
}
