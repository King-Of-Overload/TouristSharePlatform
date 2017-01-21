package alan.share.adapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class MyTypeAdapter<T> extends TypeAdapter<T> {

	@Override
	public void write(JsonWriter out, T value) throws IOException {
		if(value==null){
			out.nullValue();
			return;
		}
		out.value(value.toString());
	}

	@Override
	public T read(JsonReader in) throws IOException {
		return null;
	}
}
