// Update Kubernetes deployment manifest and push changes to GitOps repository
def call(String imageName, String imageTag) {

    // Inject GitHub Personal Access Token from Jenkins credentials
    withCredentials([string(
        credentialsId: 'github-token',
        variable: 'GIT_TOKEN'
    )]) {

        // Clone manifests repo, update image tag, commit and push changes
        sh """
            rm -rf manifests
            git clone -b main https://MaiSalama:\${GIT_TOKEN}@github.com/MaiSalama/CloudDevOpsProject-myargo.git manifests
            cd manifests

            sed -i 's|image:.*|image: ${imageName}:${imageTag}|' deployment.yaml

            git config user.email "imaisalama@gmail.com"
            git config user.name "MaiSalama"

            git add .
            git commit -m "Update image to ${imageTag}"
            git push origin main
        """
    }
}
