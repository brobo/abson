package tech.magnitude.abson;

import java.io.UnsupportedEncodingException;

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
		res[0] = (byte)(source >> 24);
		res[1] = (byte)(source >> 16 & 0xff);
		res[2] = (byte)(source >> 8 & 0xff);
		res[3] = (byte)(source & 0xff);
		return res;
	}
	
	public static byte[] toBinaryInt64(long source) {
		byte[] res = new byte[8];
		res[0] = (byte)(source >> 56);
		res[1] = (byte)(source >> 48 & 0xff);
		res[2] = (byte)(source >> 40 & 0xff);
		res[3] = (byte)(source >> 32 & 0xff);
		res[4] = (byte)(source >> 24 & 0xff);
		res[5] = (byte)(source >> 16 & 0xff);
		res[6] = (byte)(source >> 8 & 0xff);
		res[7] = (byte)(source & 0xff);
		return res;
		
	}
	
	public static byte[] toBinaryBoolean(boolean source) {
		return new byte[] {(byte)(source ? 0x01 : 0x00)};
	}
	
	public static byte[] toBinaryFloatingPoint(double source) {
		long lng = Double.doubleToLongBits(source);
		byte[] res = new byte[8];
		for (int i=0; i<8; i++) 
			res[i] = (byte)((lng >> ((7 - i) * 8)) * 0xff);
		return res;
	}
}
