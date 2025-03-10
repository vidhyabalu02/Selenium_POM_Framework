pipeline {
    agent any
    tools {
        maven 'apache-maven-3.9.9'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main' , url: 'https://github.com/vidhyabalu02/Selenium_POM_Framework.git'  
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'   
            }
        }
        stage('Test') {
            steps {
               bat 'mvn test'
            }
        }
        stage('Reports') {
			steps {
					publishHTML(target: [
					reportDir: 'src/test/resources/ExtentReports' ,
					reportFiles: 'ExtentReports.html' ,
					reporstName: 'HTML Extent Spark Report'	
					])	
			}
		}
    }
    post {
		always {
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReports/*.html' , fingerprint: true
			junit 'target/surefire-reports/*.xml'
		}
		success {
			emailext (
				to: 'vidhyabaluviba@gmail.com' ,
				subject: "Build Success: $(env.JOB_NAME) #$(env.BUILD_NUMBER)" ,
				ODY: """
				<html>
				<body> 
				<p> Hello Team, </p>
				
				<p> The latest Jenkins build has comleted. </p>
				
				<p><b>Project Name:</b> ${env.JOB_NAME} </p>
				<p><b>Build Number:</b> ${env.BUILD_NUMBER}</p> 
				<p><b>Build status:</b> <span style="color:green;"><b>SUCCESS</b></span></p>
				<p><b>Build URL:</b> <a href=${env.BUILD_URL}">${env.BUILD_URL}</A?</p>
				<p><b>Last Commit:</b></p>
				
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch: </b> ${env.GIT_BRANCH}</p>
				<p><b>Build log is attached </b></p>
				
				<p><b> Extent Report:</b> <a href = "http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20_20Report/"> Click here </a></p>
				<p>Best Regards </p>
				<p> <b>Automation Team </b></p>
				</body>
				</html>
				""",
				mimeType:'text/html' ,
				attachlog: true
			)
		}
		failure {
			emailext (
				to: 'vidhyabaluviba@gmail.com' ,
				subject: "Build Failed: $(env.JOB_NAME) #$(env.BUILD_NUMBER)" ,
				ODY: """
				<html>
				<body> 
				<p> Hello Team, </p>
				
				<p> The latest Jenkins build has <b style="color :red; ">FAILED </b>. </p>
				
				<p><b>Project Name:</b> ${env.JOB_NAME} </p>
				<p><b>Build Number:</b> ${env.BUILD_NUMBER}</p> 
				<p><b>Build status:</b> <span style="color:red;"><b>FAILED</b></span></p>
				<p><b>Build URL:</b> <a href=${env.BUILD_URL}">${env.BUILD_URL}</A?</p>
				<p><b>Last Commit:</b></p>
				
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch: </b> ${env.GIT_BRANCH}</p>
				<p><b>Build log is attached </b></p>
				
				<p> <b> Please check the log and take necessary actions.</b></p>
				
				<p><b> Extent Report:</b> <a href = "http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20_20Report/"> Click here </a></p>
				<p>Best Regards </p>
				<p> <b>Automation Team </b></p>
				</body>
				</html>
				""",
				mimeType:'text/html' ,
				attachlog: true
			)
		}
	}
}