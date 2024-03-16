//package auto;
//
//public class HTMLCrawler {
//    private Set<String> visitedUrls;
//    private Queue<String> urlsToVisit;
//    private int maxUrlsToVisit;
//
//    public WebCrawler(int maxUrlsToVisit) {
//        visitedUrls = new HashSet<String>();
//        urlsToVisit = new LinkedList<String>();
//        this.maxUrlsToVisit = maxUrlsToVisit;
//    }
//
//    public void crawl(String startingUrl, String saveDir) throws IOException {
//        File dir = new File(saveDir);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        urlsToVisit.add(startingUrl);
//        while (!urlsToVisit.isEmpty() && visitedUrls.size() < maxUrlsToVisit) {
//            String url = urlsToVisit.poll();
//            if (!visitedUrls.contains(url)) {
//                visitedUrls.add(url);
//                System.out.println("Visiting: " + url);
//                String links = HTMLParser.parse(url, saveDir);
//                for (String nextUrl : links.split(" ")) {
//                    if (!visitedUrls.contains(nextUrl)) {
//                        urlsToVisit.add(nextUrl);
//                    }
//                }
//            }
//        }
//        System.out.println("Website is crawled!");
//    }
//}