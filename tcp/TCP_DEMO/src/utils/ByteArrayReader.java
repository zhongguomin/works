package utils;

/**
 * 可以从字节数组中读取所需要的数据
 *
 */
public class ByteArrayReader {
	private int point=0;
	private byte [] buf = null;
	
	public ByteArrayReader(byte [] b){
		buf=b;
	}
	
	public int readInt(int offset){
		int i = buf[offset++];
		i=((i<<8)|buf[offset++]);
		i=((i<<8)|buf[offset++]);
		i=((i<<8)|buf[offset++]);
		return i;
	}
	public byte readByte(int offset){
		return buf[offset];
	}
	
	public int readInt(){
		int i = buf[point++];
		i=((i<<8)|buf[point++]);
		i=((i<<8)|buf[point++]);
		i=((i<<8)|buf[point++]);
		return i;
	}
	
	public byte readByte(){
		return readByte(point++);
	}
}