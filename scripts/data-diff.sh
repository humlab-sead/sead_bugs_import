#!/bin/bash
# https://docs.datafold.com/reference/open_source/cli/

data-diff \
    postgresql://humlab_admin:Vua9VgaZ@localhost:5432/sead_production_201912 \
    tbl_sites \
    postgresql://humlab_admin:Vua9VgaZ@localhost:5432/sead_staging \
    tbl_sites \
    -k site_id \
    -c altitude -c latitude_dd -c longitude_dd -c national_site_identifier -c site_description -c site_name -c site_preservation_status_id -c site_location_accuracy \
    -t date_updated

    #--json
#    -w <filter condition>:w!