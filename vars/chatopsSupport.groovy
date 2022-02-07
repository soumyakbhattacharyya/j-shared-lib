@Grab(group='com.github.groovy-wslite', module='groovy-wslite', version='1.1.3')
import net.sf.json.groovy.JsonSlurper

def call(Map args) {
	
    if (args.action == 'comment') {
	    return comment(args.token)
    }	
	
    if (args.action == 'check') {
	    return check(args.token)
    }
    if (args.action == 'terminateEnvironment') {
        return terminateEnvironment()
    }
    error 'chatopsSupport has been called without valid arguments'
}

def comment(token){
	
   println "token is following " + token	
 
    def user = "soumyakbhattacharyya"
    def tokenId = token
    def url = "https://api.github.com/repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments"
    //def command = "curl -i -X POST -H \"Accept: application/vnd.github.v3+json\" -u $user:$token $url -d @params.json"
    //println command	
    //println command.execute().text
    ["curl", "-i", "-X POST", "-H 'Content-Type:application/vnd.github.v3+json'", "-u $user:$tokenId","-d '{\"body\":\"comment from Jenkins\"}'", "https://api.github.com/repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments"].execute().text	
}

def check(token) {

    println token	
	
    def user = "soumyakbhattacharyya"
    def tokenId = token
    def url = "https://api.github.com/repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments"
    def command = "curl -i -u $user:#tokenId $url"
    
    def count = 1;	
    while(count<20) {
       count++	    
       println command.execute().text	       
       def retVal = command.execute().text.drop(1175).trim()
       def list = new JsonSlurper().parseText(retVal)
       println list.last()	       
       println list.last().body

       if (list.last().body == "TERMINATE") 
	    break
	    
       Thread.sleep(5000)	    
    }
}

def terminateEnvironment() {
    if (env.DESTROY_ENVIRONMENT == "false") {
        echo 'keeping environment as it is'
    }else{
		echo 'destroying environment'
	}
}
