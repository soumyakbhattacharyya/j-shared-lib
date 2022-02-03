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
    def command = "curl -i -u $user:#tokenId $url -d '{\"body\":\"please inform if environment needs to be kept intact\"}'"
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
