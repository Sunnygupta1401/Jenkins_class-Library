def call(body) {
 def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    

    
    pipeline {
        agent any
     tools
     {
      
      maven 'localMaven'
     }
        stages {
            stage('checkout git') {
                steps {
                    git branch: pipelineParams.branch, url: pipelineParams.scmUrl
                 echo pipelineParams.check

                }
            }

            stage('build') {
                steps {
                    sh 'mvn clean package package'
                               // sh "'${mvnHome}/bin/mvn' clean package"

                    echo "build"
                }
            }

            stage ('Archive') {
                steps {
           archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
                }
            }

            

            stage('deploy staging'){
                steps {
//build 'deploy-to-staging'

                 sh "cp **/target/*.war" pipelineParams.stagingServer

                }
            }

            stage('deploy production'){
                steps {
//build 'deploy-to-production'               
                 
            sh 'cp **/target/*.war     /Users/sunnygupta/Documents/apache-tomcat-8.5.20-production.20/webapps'

                 
                }
            }
        }
        post {
            failure {
                mail to: pipelineParams.email, subject: 'Pipeline failed', body: "${env.BUILD_URL}"
            }
        }
    }
}
