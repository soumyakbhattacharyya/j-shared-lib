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
    def command = "curl -i -X POST -H \"Accept: application/vnd.github.v3+json\" -u $user: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 $url -d '{\"body\":\"body12345\"}'"
    println command	
    println command.execute().text	
}

def check() {
    def user = "soumyakbhattacharyya"
    def tokenId = "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"
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
