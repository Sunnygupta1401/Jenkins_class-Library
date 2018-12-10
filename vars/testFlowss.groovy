def call(body) {
 def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    
    
   node {
   def mvnHome
   def bb
   def GIT_BRANCH
   
            stage('Preparation') {
            git branch: pipelineParams.branch, url: pipelineParams.scmUrl
                 echo pipelineParams.check
                       mvnHome = tool name: 'localMaven', type: 'maven'

             if (fileExists("${env.WORKSPACE}/jenkins.properties"))
                {
                 echo "yes"
                              

                }
                // echo "${pipelineParams.personDetails.firstName}"
            }
            stage('build') {
               
                    //sh 'mvn clean package package'
                   sh "'${mvnHome}/bin/mvn' clean package"

                    echo "build"
                
            }

            stage ('Archive') {
               
           archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
                
            }

            

            stage('deploy staging'){
                
//build 'deploy-to-staging'

      //  sh 'cp **/target/*.war /Users/sunnygupta/Documents/apache-tomcat-8.5.20/webapps'

                 sh "cp **/target/*.war ${pipelineParams.stagingServer}"

                
            }

            stage('deploy production'){
                
//build 'deploy-to-production'               
                 
            sh 'cp **/target/*.war     /Users/sunnygupta/Documents/apache-tomcat-8.5.20-production.20/webapps'

                 
                
            }
        }
} 
