package com.univers_architecture.solr.api.solrJ.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.univers_architecture.solr.api.solrJ.model.Film;


public class FilmSearchServiceLiveTest {

    private static SolrClient solrClient;
    private static FilmSearchService filmSearchService;
    private static final String solrUrl = "http://localhost:8983/solr/films";
    
    
    
   
	

	@BeforeClass
    public static void initBeans() throws Exception {
        solrClient = new HttpSolrClient.Builder(solrUrl).build();
        filmSearchService = new FilmSearchServiceImpl(solrClient);

        solrClient.commit();
    }

    @Before
    public void clearSolrData() throws Exception {
        solrClient.deleteByQuery("*:*");
    }

    @Test
    public void whenIndexing_thenAvailableOnRetrieval() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
		
        filmSearchService.index("123456", "quentin tarantino", "django unchained","2017-10-15", django_categories, (float) 1000000);
        final SolrDocument indexedDoc = solrClient.getById("123456");
        assertEquals("123456", indexedDoc.get("id"));
        solrClient.deleteByQuery("*:*");
    }

    @Test
    public void whenIndexingBean_thenAvailableOnRetrieval() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
		ArrayList<String> values = new ArrayList<String>(Arrays.asList(django_categories));

        Film film = new Film();
        film.setId("123456");
        film.setDirected_by("quentin tarantino");
        film.setName("django unchained");
        film.setPublished_date("2017-10-15");
        film.setCategory(values);
        film.setRevenue(1000000);
        filmSearchService.indexBean(film);
        solrClient.deleteByQuery("*:*");
    }

    @Test
    public void whenSearchingByBasicQuery_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

        filmSearchService.index("444", "quentin tarantino", "movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "saw","2012-10-15", saw_categories, (float) 100);
        filmSearchService.index("123456", "marvel", "movie1 captain america","2018-10-15", marvel_categories, (float) 100);

        SolrQuery query = new SolrQuery();
        query.setQuery("movie1");
        query.setStart(0);
        query.setRows(10);

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
       // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
        

        //List<Film> films = response.getBeans(Film.class);

        assertEquals(2, moviesDocumentList.getNumFound());
        solrClient.deleteByQuery("*:*");


    }

    @Test
    public void whenSearchingWithWildCard_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "saw","2012-10-15", saw_categories, (float) 100);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 100);

        SolrQuery query = new SolrQuery();
        query.setQuery("*movie?");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(2, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }

    @Test
    public void whenSearchingWithLogicalOperators_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 100);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 100);

        SolrQuery query = new SolrQuery();
        query.setQuery("movie1 AND (marvel OR tarantino)");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(2, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }
   
    @Test
    public void whenSearchingWithFields_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 100);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 100);


        SolrQuery query = new SolrQuery();
        query.setQuery("name:django* AND directed_by:*tarantino");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(1, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }
    
    @Test
    public void whenSearchingWithPhrase_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 100);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 100);



        SolrQuery query = new SolrQuery();
        query.setQuery("django unchained");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(1, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }
    
    @Test
    public void whenSearchingWithRealPhrase_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 100);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 100);


        SolrQuery query = new SolrQuery();
        query.setQuery("\"django unchained\"");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(1, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }
    
   /* @Test
    public void whenSearchingPhraseWithProximity_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 100);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 100);

        SolrQuery query = new SolrQuery();
        query.setQuery("\"quentin spielberg\"~1");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(1, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }*/

    @Test
    public void whenSearchingWithRevenueRange_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 200);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 300);

        SolrQuery query = new SolrQuery();
        query.setQuery("revenue:[100 TO 300]");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(3, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }
    
    @Test
    public void whenSearchingWithRevenueRangeInclusiveExclusive_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "thriller"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 200);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 300);
        SolrQuery query = new SolrQuery();
        query.setQuery("revenue:{100 TO 300]");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(2, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }
    
    @Test
    public void whenSearchingWithFilterQuery_thenAllMatchingFilmsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "comedy"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 200);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 300);

        SolrQuery query = new SolrQuery();
        query.setQuery("revenue:[100 TO 300]");
        query.addFilterQuery("name:cool", "category:comedy");

        QueryResponse response = solrClient.query(query);
        SolrDocumentList moviesDocumentList = response.getResults();
        
        // List<Film> films =  binder.getBeans(Film.class, moviesDocumentList);
         

         //List<Film> films = response.getBeans(Film.class);

         assertEquals(1, moviesDocumentList.getNumFound());
         solrClient.deleteByQuery("*:*");
    }
    
    @Test
    public void whenSearchingWithFacetFields_thenAllMatchingFacetsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "comedy"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 200);
        filmSearchService.index("123456", "marvel", "bad movie1 captain america","2018-10-15", marvel_categories, (float) 300);
        
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addFacetField("category");

        QueryResponse response = solrClient.query(query);
        List<Count> facetResults = response.getFacetField("category").getValues();
        System.out.println(facetResults);
        assertEquals(4, facetResults.size());

        for (Count count : facetResults) {
        	System.out.println(count.getName());
            if ("action".equalsIgnoreCase(count.getName())) {
                assertEquals(3, count.getCount());
            } else if ("adventure".equalsIgnoreCase(count.getName())) {
                assertEquals(3, count.getCount());
                
            }else if ("comedy".equalsIgnoreCase(count.getName())) {
                assertEquals(1, count.getCount());
                }
           
        }
        solrClient.deleteByQuery("*:*");

    }
   
    @Test
    public void whenSearchingWithFacetQuery_thenAllMatchingFacetsShouldAvialble() throws Exception {
    	String[] django_categories = {"action", "adventure", "comedy"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 200);
        filmSearchService.index("123456", "marvel director", "bad movie2 captain america","2018-10-15", marvel_categories, (float) 300);
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");

        query.addFacetQuery("django OR saw");
        query.addFacetQuery("director");

        QueryResponse response = solrClient.query(query);
        Map<String, Integer> facetQueryMap = response.getFacetQuery();

        assertEquals(2, facetQueryMap.size());

        for (Map.Entry<String, Integer> entry : facetQueryMap.entrySet()) {
            String facetQuery = entry.getKey();

            if ("django OR saw".equals(facetQuery)) {
                assertEquals(Integer.valueOf(2), entry.getValue());
            } else if ("director".equals(facetQuery)) {
                assertEquals(Integer.valueOf(2), entry.getValue());
            } 

        }
        solrClient.deleteByQuery("*:*");

    }
    
    @Test
    public void whenSearchingWithFacetRange_thenAllMatchingFacetsShouldAvialble() throws Exception {
      String[] django_categories = {"action", "adventure", "comedy"} ;
    	String[] saw_categories = {"action", "adventure", "thriller"} ;
    	String[] marvel_categories = {"action", "adventure", "thriller"} ;

    	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 200);
        filmSearchService.index("123456", "marvel director", "bad movie2 captain america","2018-10-15", marvel_categories, (float) 300);
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");

        query.addNumericRangeFacet("revenue", 100, 275, 25);

        QueryResponse response = solrClient.query(query);
        List<RangeFacet> rangeFacets = response.getFacetRanges().get(0).getCounts();

        assertEquals(7, rangeFacets.size());
        solrClient.deleteByQuery("*:*");

    }
    
    @Test
    public void whenSearchingWithHitHighlighting_thenKeywordsShouldBeHighlighted() throws Exception {
    	String[] django_categories = {"action", "adventure", "comedy"} ;
     	String[] saw_categories = {"action", "adventure", "thriller"} ;
     	String[] marvel_categories = {"action", "adventure", "thriller"} ;

     	filmSearchService.index("444", "quentin tarantino", "cool movie1 django unchained","2017-10-15", django_categories, (float) 100);
        filmSearchService.index("333", "saw's director", "nice movie1 saw","2012-10-15", saw_categories, (float) 200);
        filmSearchService.index("123456", "marvel director", "bad movie2 captain america","2018-10-15", marvel_categories, (float) 300);

        SolrQuery query = new SolrQuery();
        query.setQuery("movie1");
        query.setHighlight(true);
        query.addHighlightField("name");
        query.setHighlightSimplePre("<strong>");
        query.setHighlightSimplePost("</strong>");
        QueryResponse response = solrClient.query(query);

        Map<String, Map<String, List<String>>> hitHighlightedMap = response.getHighlighting();
        Map<String, List<String>> highlightedFieldMap = hitHighlightedMap.get("444");
        List<String> highlightedList = highlightedFieldMap.get("name");
        String highLightedText = highlightedList.get(0);

        assertEquals("cool <strong>movie1</strong> django unchained", highLightedText);

    }
    /*  
    @Test
    public void whenSearchingWithKeywordWithMistake_thenSpellingSuggestionsShouldBeReturned() throws Exception {
        filmSearchService.index("hm0001", "Brand1 Washing Machine", "Home Appliances", 100f);
        filmSearchService.index("hm0002", "Brand1 Refrigerator", "Home Appliances", 300f);
        filmSearchService.index("hm0003", "Brand2 Ceiling Fan", "Home Appliances", 200f);
        filmSearchService.index("hm0004", "Brand2 Dishwasher", "Washing equipments", 250f);

        SolrQuery query = new SolrQuery();
        query.setQuery("hme");
        query.set("spellcheck", "on");
        QueryResponse response = solrClient.query(query);

        SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();

        assertEquals(false, spellCheckResponse.isCorrectlySpelled());

        Suggestion suggestion = spellCheckResponse.getSuggestions().get(0);

        assertEquals("hme", suggestion.getToken());

        List<String> alternatives = suggestion.getAlternatives();
        String alternative = alternatives.get(0);

        assertEquals("home", alternative);
    }
    
    @Test
    public void whenSearchingWithIncompleteKeyword_thenKeywordSuggestionsShouldBeReturned() throws Exception {
        filmSearchService.index("hm0001", "Brand1 Washing Machine", "Home Appliances", 100f);
        filmSearchService.index("hm0002", "Brand1 Refrigerator", "Home Appliances", 300f);
        filmSearchService.index("hm0003", "Brand2 Ceiling Fan", "Home Appliances", 200f);
        filmSearchService.index("hm0004", "Brand2 Dishwasher", "Home washing equipments", 250f);

        SolrQuery query = new SolrQuery();
        query.setRequestHandler("/suggest");
        query.set("suggest", "true");
        query.set("suggest.build", "true");
        query.set("suggest.dictionary", "mySuggester");
        query.set("suggest.q", "Hom");
        QueryResponse response = solrClient.query(query);

        SuggesterResponse suggesterResponse = response.getSuggesterResponse();
        Map<String, List<String>> suggestedTerms = suggesterResponse.getSuggestedTerms();
        List<String> suggestions = suggestedTerms.get("mySuggester");

        assertEquals(2, suggestions.size());

        for (String term : suggestions) {
            if (!"Home Appliances".equals(term) && !"Home washing equipments".equals(term)) {
                fail("Unexpected suggestions");
            }
        }

    }
*/
}
