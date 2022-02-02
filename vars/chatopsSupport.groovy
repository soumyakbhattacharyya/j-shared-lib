@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')
import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class GithubSearchService {

    private String authToken

    public GithubSearchService(String authToken) {
        this.authToken = authToken
    }

    public void search(String query) {
        def http = new HTTPBuilder('https://api.github.com')

        http.request(GET, JSON) { req ->
            uri.path = 'repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments'
            headers.'Authorization' = "token $authToken"
            headers.'Accept' = 'application/vnd.github.v3.text-match+json'
            headers.'User-Agent' = 'Mozilla/5.0'
            response.success = { resp, json ->
                println "Got response: ${resp.statusLine}"
                println "Content-Type: ${resp.headers.'Content-Type'}"
                println json
            }
            response.failure = { resp, json ->
                print json
            }
        }
    }
}

def call(Map args) {

  	new GithubSearchService('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').search()


    if (args.action == 'check') {
	    return check()
    }
    if (args.action == 'terminateEnvironment') {
        return terminateEnvironment()
    }
    error 'chatopsSupport has been called without valid arguments'
}

public Map request(String url) {
        githubApi.get(path : url).responseData
}

def check() {
    env.DESTROY_ENVIRONMENT = "true" // flag to destroy test environment as a default behavior
    result = sh (script: "git log -1 | grep 'KEEP_ON_FAILURE'", returnStatus: true)
    if (result == 0) {
        env.DESTROY_ENVIRONMENT = "false" // keep test environment 
        println "'[KEEP_ON_FAILURE]' found in git commit message. Keeping test environment intact."
    }
}

def terminateEnvironment() {
    if (env.DESTROY_ENVIRONMENT == "false") {
        echo 'keeping environment as it is'
    }else{
		echo 'destroying environment'
	}
}