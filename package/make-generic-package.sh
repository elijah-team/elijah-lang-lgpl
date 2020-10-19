VERSION=0.0.6

rm -rf package/bin package/share
mkdir -p package
mkdir -p package/bin
mkdir -p package/share/elijjah/$VERSION/lib/stdlib/
mkdir -p package/share/elijjah/$VERSION/compiler/
mkdir -p package
mkdir -p package
mkdir -p package
mvn -Dmaven.test.skip=true package
cp -a target/elijah-v1-$VERSION.jar package/share/elijjah/$VERSION/compiler/
mvn dependency:copy-dependencies -DoutputDirectory=package/share/elijjah/$VERSION/compiler/
cp -a lib_elijjah/lib-c package/share/elijjah/$VERSION/lib/stdlib/
tar zcf elijjah-$VERSION.tgz bin share
