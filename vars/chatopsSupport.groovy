import net.sf.json.groovy.JsonSlurper

def call(Map args) {
	
    if (args.action == 'comment') {
	    return comment()
    }	
	
    if (args.action == 'check') {
	    return check()
    }
    if (args.action == 'terminateEnvironment') {
        return terminateEnvironment()
    }
    error 'chatopsSupport has been called without valid arguments'
}

def comment(){
 
    def user = "soumyakbhattacharyya"
    def tokenId = "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"
    def url = "https://api.github.com/repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments"
    def command = "curl -i -X POST -H \"Accept: application/vnd.github.v3+json\" -u $user:$tokenId $url -d '{\"body\":\"body\"}'"
    println command	
    println command.execute().text	
}

def check() {
    def user = "soumyakbhattacharyya"
    def tokenId = "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"
    def url = "https://api.github.com/repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments"
    def command = "curl -i -u $user:$tokenId $url"
    
    def count = 1;	
    while(count<20) {
       count++	     
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
