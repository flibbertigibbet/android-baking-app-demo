# https://github.com/Giphy/giphy-android-sdk-core/blob/master/.travis-android-license-bugfix.sh

echo "$ANDROID_HOME"
mkdir "$ANDROID_HOME/licenses" || true
echo -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" > "$ANDROID_HOME/licenses/android-sdk-license"
echo -e "\n84831b9409646a918e30573bab4c9c91346d8abd" > "$ANDROID_HOME/licenses/android-sdk-preview-license"

