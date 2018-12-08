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

            stage('deploy developmentServer'){
                steps {
                    echo "dev started"
                }
            }

            stage('deploy staging'){
                steps {
                    echo "stag started"
                }
            }

            stage('deploy production'){
                steps {
                    echo "prod started"
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
