import net.sf.json.groovy.JsonSlurper

def call(Map args) {
	
    def user = "soumyakbhattacharyya"
    def tokenId = "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"
    def url = "https://api.github.com/repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments"
    def command = "curl -i -u $user:#tokenId $url"
    
    println command.execute().text.drop(1175) 	
	
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

    if (args.action == 'check') {
	    return check()
    }
    if (args.action == 'terminateEnvironment') {
        return terminateEnvironment()
    }
    error 'chatopsSupport has been called without valid arguments'
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
