import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * test-lucene
 * Created by IntelliJ IDEA.
 * Developer: Maksim Kulikov
 * Date: 12.07.2022
 * Time: 13:12
 */
public class DataStore {

    private final Analyzer analyzer = new WhitespaceAnalyzer();
    private final Directory directory = new ByteBuffersDirectory();

    public DataStore() {
    }

    public IndexWriter getNewIndexWriter() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(directory, config);
    }

    public IndexSearcher getSearcher() {
        IndexSearcher searcher = null;
        try {
            IndexReader reader = DirectoryReader.open(directory);
            searcher = new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searcher;
    }

    public QueryParser getQueryParser(final String documentName) {
        QueryParser parser = new QueryParser(documentName, analyzer);
        parser.setAllowLeadingWildcard(true);
        return parser;
    }

    public TopDocs search(final String documentName, final String query, int count) {
        TopDocs result = null;
        try {
            QueryParser parser = getQueryParser(documentName);
            Query queryObj = parser.parse(query);
            result = getSearcher().search(queryObj, count);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
