cd ./gridlayout_v7
android update project --target 4 -p .
ant clean
cd ../app
android update project --target 4 -p .
ant clean
ant debug