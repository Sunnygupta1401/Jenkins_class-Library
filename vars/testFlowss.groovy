def call(body) {
 def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    
   node {
   def mvnHome
   def bb
   def GIT_BRANCH
   Properties props = new Properties()

            stage('Preparation') {
            git branch: pipelineParams.branch, url: pipelineParams.scmUrl
                 echo pipelineParams.check
                       mvnHome = tool name: 'localMaven', type: 'maven'

             if (fileExists("${env.WORKSPACE}/jenkins.properties"))
                {
                 echo "yes"
          File propsFile = new File("${env.WORKSPACE}/jenkins.properties")

props.load(propsFile.newDataInputStream())
                   
                 
                   
test = props.getProperty('stagingServer')
                 echo props.getProperty('a')
                    
                   
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

                // sh "cp **/target/*.war ${pipelineParams.stagingServer}"
             
             sh "cp **/target/*.war ${props.getProperty('stagingServer')}"

                
            }

            stage('deploy production'){
                
//build 'deploy-to-production'               
                 
            sh 'cp **/target/*.war     /Users/sunnygupta/Documents/apache-tomcat-8.5.20-production.20/webapps'

                 
                
            }
        }
} 
