
reset-debug-db:
	@../sead_change_control/bin/copy-database --source sead_production_201912 --target sead_production_201912_debug --force
	@scripts/copy-table --host humlabseadserv.srv.its.umu.se --user humlab_admin --source-database bugsdata_20200219 --target-database sead_production_201912 --source-table INDEX --target-schema bugsdata
	@scripts/copy-table --host humlabseadserv.srv.its.umu.se --user humlab_admin --source-database bugsdata_20200219 --target-database sead_production_201912 --source-table TSynonym --target-schema bugsdata

clean:
	@mvn -Dmaven.test.skip=true clean

package:
	@mvn -Dmaven.test.skip=true package

.PHONY: config
config:
	@cat ./config/application.properties

tools:
	@@sudo apt install mdbtools unixodbc
