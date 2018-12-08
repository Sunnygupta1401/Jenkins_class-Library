def call(body) {
 def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    
    
    
    pipeline {
        agent any
        stages {
            stage('checkout git') {
                steps {
                    git branch: pipelineParams.branch, url: pipelineParams.scmUrl
                 echo pipelineParams.check
                       mvnHome = tool name: 'localMaven', type: 'maven'

                }
            }

            stage('build') {
                steps {
                    //sh 'mvn clean package -DskipTests=true'
                                sh "'${mvnHome}/bin/mvn' clean package"

                    echo "build"
                }
            }

            stage ('test') {
                steps {
                    echo "test started"
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
