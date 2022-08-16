import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

/**
 * test-lucene
 * Created by IntelliJ IDEA.
 * Developer: Maksim Kulikov
 * Date: 12.07.2022
 * Time: 11:56
 */
public class Main {

    private static final DataStore dataStore = new DataStore();

    public static void main(String[] args) throws IOException {
        try (IndexWriter writer = dataStore.getNewIndexWriter()) {
            addDoc(writer, "Lucene in Action", "193398817");
            addDoc(writer, "Lucene for Dummies", "55320055Z");
            addDoc(writer, "Managing Gigabytes", "55063554A");
            addDoc(writer, "The Art of Computer Science", "99003323X");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // нечеткое соответствие '*'
        TopDocs results = dataStore.search("title", "manag*", 5);
        print(results);

        // =============
        results = dataStore.search("title", "Dummies", 5);
        print(results);

        // =============
        results = dataStore.search("isbn", "99*", 5);
        print(results);

        // =============
        results = dataStore.search("isbn", "55*", 1);
        print(results);
    }

    private static void print(final TopDocs results) throws IOException {
        ScoreDoc[] hits = results.scoreDocs;
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = dataStore.getSearcher().doc(docId);
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
        }
    }

    private static void addDoc(final IndexWriter writer, final String title, final String isbn) throws IOException {
        Document document = new Document();
        document.add(new TextField("title", title, Field.Store.YES));
        document.add(new StringField("isbn", isbn, Field.Store.YES));

        writer.addDocument(document);
    }
}
