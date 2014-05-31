package tech.magnitude.abson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class BsonUtil {

	/**
	 * Converts a String to its byte representation.
	 * @param source The string to convert.
	 * @return The converted byte array.
	 */
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
	
	/**
	 * Converts a String to its byte representation via toBinaryCString(), then prefixes
	 * the String with its length.
	 * @param source The String to convert.
	 * @return The converted byte array.
	 */
	public static byte[] toBinaryString(String source) {
		byte[] bytes = toBinaryCString(source);
		byte[] res = new byte[bytes.length + 4];
		System.arraycopy(bytes, 0, res, 4, bytes.length);
		System.arraycopy(toBinaryInt32(bytes.length), 0, res, 0, 4);
		return res;
	}
	
	/**
	 * Alias for toBinaryCString.
	 * @param source The string to convert.
	 * @return The converted byte array.
	 */
	public static byte[] toBinaryEString(String source) {
		//alias for cstring
		return toBinaryCString(source);
	}
	
	/**
	 * Obtains a byte array which contains the BSON representation of a specified Abson object.
	 * @param object The object to convert to its BSON representation.
	 * @return A byte array containing the BSON representation of the object.
	 * @throws IOException Thrown if an error occurs while converting the object to BSON. (Though this should never happen)
	 */
	public static byte[] getArray(AbsonValue object) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		object.toBson(out);
		
		return out.toByteArray();
	}
	
	/* ************** *
	 * * Primitives * *
	 * ************** */
	
	public static byte[] toBinaryInt32(int source) {
		byte[] res = new byte[4];
		for (int i=0; i<4; i++) {
			res[i] = (byte)(source & 0xff);
			source >>>= 8;
		}
		return res;
	}
	
	public static int fromBinaryInt32(byte[] bytes) {
		int res = 0;
		for (int i=3; i>=0; i--) {
			res <<= 8;
			res |= bytes[i] & 0xff;
		}
		return res;
	}
	
	public static byte[] toBinaryInt64(long source) {
		byte[] res = new byte[8];
		for (int i=0; i<8; i++) {
			res[i] = (byte)(source & 0xff);
			source >>>= 8;
		}
		return res;
	}
	
	public static long fromBinaryInt64(byte[] bytes) {
		long res = 0;
		for (int i=7; i>=0; i--) {
			res <<= 8;
			res |= bytes[i] & 0xff;
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
