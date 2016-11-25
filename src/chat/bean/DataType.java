package chat.bean;

public enum DataType {
	CHAR,
	BYTE,
	SHORT,
	INT,
	LONG,
	STRING;
	
	// 一个字节多少位
	public static final int BIT_SIZE = 8;

	// 整数字节数
	public static final int INT_SIZE = Integer.SIZE / BIT_SIZE;

	// long 字节数
	public static final int LONG_SIZE = Long.SIZE / BIT_SIZE;
	
	/**
	 * 
	 * @return 返回数据类型的长度，如果不知道则返回0
	 */
	public int getLength() {
		return getLength(this);
	}
	
	/**
	 * 
	 * @return 返回数据类型的长度，如果不知道则返回0
	 */
	public static int getLength(DataType type) {
		int len = 0;
		switch (type) {
		case CHAR:
			len = Character.SIZE / BIT_SIZE;
			break;
		case BYTE:
			len = Byte.SIZE / BIT_SIZE;
			break;
		case SHORT:
			len = Short.SIZE / BIT_SIZE;
			break;
		case INT:
			len = Integer.SIZE / BIT_SIZE;
			break;
		case LONG:
			len = Long.SIZE / BIT_SIZE;
			break;
		}
		return len;
	}
}
