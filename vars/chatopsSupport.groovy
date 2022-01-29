def call(Map args) {
    if (args.action == 'check') {
	    println "within check method"
        return check()
    }
    if (args.action == 'postProcess') {
        return postProcess()
    }
    error 'ciSkip has been called without valid arguments'
}

def check() {
    env.CI_SKIP = "false"
    result = sh (script: "git log -1 | grep 'KEEP_ON_FAILURE'", returnStatus: true)
	println "result " + result
    if (result == 0) {
        env.CI_SKIP = "true"
        println "'[KEEP_ON_FAILURE]' found in git commit message. Keeping test environment intact."
    }
}

def postProcess() {
    if (env.CI_SKIP == "true") {
        currentBuild.result = 'NOT_BUILT'
    }
}