import groovy.json.JsonSlurper

def call(Map args) {

  	String userPassBase64 = "soumyakbhattacharyya:Helpdesk@0202".toString().bytes.encodeBase64()    
	def github = new HTTPBuilder('https://api.github.com/')
	def emails = github.get(path: 'repos/soumyakbhattacharyya/to-be-used-for-jenkins-poc/issues/1/comments', headers: ["Authorization": "Basic $userPassBase64"])
	def json = JsonSlurper().parseText(emails)
	def bodyText = json.body


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