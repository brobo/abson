package tech.magnitude.abson.test;

import tech.magnitude.abson.AbsonComposer;
import tech.magnitude.abson.AbsonDecomposable;
import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonObject;

public class TestUser implements AbsonDecomposable {
	String name;
	int age;
	Book[] favorites;
	
	public TestUser(String name, int age, Book... favorites) {
		this.name = name;
		this.age = age;
		this.favorites = favorites;
	}
	
	@Override
	public AbsonObject decompose() {
		AbsonObject res = new AbsonObject();
		res.put("name", name);
		res.put("age", age);
		AbsonArray absbook = new AbsonArray();
		for (Book cur : favorites) {
			absbook.add(cur.decompose());
		}
		res.put("favorites", absbook);
		return res;
	}

	public static TestUser compose(AbsonObject abson) {
		String name = abson.getString("name");
		int age = abson.getInteger("age");
		AbsonArray booksArr = abson.getArray("favorites");
		Book[] books = new Book[booksArr.size()];
		for (int i=0; i<booksArr.size(); i++) {
			books[i] = Book.compose(booksArr.getObject(i));
		}
		return new TestUser(name, age, books);
	}
	
	public static final TestUser COLBY = new TestUser("Colby Brown", 17,Book.MartianChronicles, Book.Anthem);
	public static final TestUser TRES = new TestUser("Tres Brenson", 16, Book.JLS7);
	
	static class Book implements AbsonDecomposable {
		String title;
		double rating;
		
		public Book(String title, double rating) {
			this.title = title;
			this.rating = rating;
		}
		
		public static final Book MartianChronicles = new Book("The Martian Chronicles", 4.5);
		public static final Book Anthem = new Book("Anthem", 1.0);
		public static final Book JLS7 = new Book("Java Language Specifications 1.7", 3.25);

		@Override
		public AbsonObject decompose() {
			AbsonObject res = new AbsonObject();
			res.put("title", title);
			res.put("rating", rating);
			return res;
		}
		
		public static Book compose(AbsonObject abson) {
			return new Book(abson.getString("title"), abson.getDouble("rating"));
		}
	}
}
