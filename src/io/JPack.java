package io;

import java.security.Key;

public interface JPack {
	int size = 16;
	// 書庫作成
	public void pack(Key key);
	// 書庫解凍
	public void unpack(Key key);
}
