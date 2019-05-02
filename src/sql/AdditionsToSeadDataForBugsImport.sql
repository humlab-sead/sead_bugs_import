ALTER TABLE tbl_taxa_measured_attributes
   ALTER COLUMN attribute_type TYPE character varying(255);
ALTER TABLE tbl_taxa_measured_attributes
   ALTER COLUMN attribute_measure TYPE character varying(255);

insert into tbl_species_association_types (association_type_name) values ('dung in burrows parasitised by');
insert into tbl_species_association_types (association_type_name) values ('found in same log as');
insert into tbl_species_association_types (association_type_name) values ('found breeding in same log as');
insert into tbl_species_association_types (association_type_name) values ('in same habitat as?');
insert into tbl_species_association_types (association_type_name) values ('in same habitat, but not in burials, as');
insert into tbl_species_association_types (association_type_name) values ('in same pools as');
insert into tbl_species_association_types (association_type_name) values ('in same upland habitat as');
insert into tbl_species_association_types (association_type_name) values ('in same upland pools as');
insert into tbl_species_association_types (association_type_name) values ('in same wood as');
insert into tbl_species_association_types (association_type_name) values ('is kleptoparasitic in burrows of');
insert into tbl_species_association_types (association_type_name) values ('is kleptoparasitic in nests of');
insert into tbl_species_association_types (association_type_name) values ('is kleptoparasitic on');
insert into tbl_species_association_types (association_type_name) values ('is kleptoparasitised by');
insert into tbl_species_association_types (association_type_name) values ('is loosely associated with');
insert into tbl_species_association_types (association_type_name) values ('predated on by');

insert into tbl_dating_labs (international_lab_id, lab_name) values ('Unknown', 'Unknown or unspecified');insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Cal',20,'Calibrated radiocarbon date (method unspecified)',null,8,'Calendar years date provided by the calibration of a radiocarbon age. Exact dating method unspecified.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('CalAMS',20,'Calibrated AMS radiocarbon date',null,8,'Calendar years date provided by calibration of an AMS radiocarbon age.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('GeolPer',19,'Geological period (unspecified years)',null,15,'Geological period with unspecified years type');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Interp 14C',3,'Interpolated C14','19',7,'Dating using the interpolation of 14C dates from other samples (give details in dating notes)');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('ESR',21,'Electron Spin Resonance','19',15,'Dating based on the trapped charges of electrons, electron spin resonance dating.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('He-U',3,'Helium-Uranium','19',15,'Dating based on the production of helium during the radioactive decay of uranium and thorium.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Cl36',3,'Chlorine 36','19',15,'Dating based on the radioactive decay of chlorine 36');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('U-Trend',3,'Uranium trend','19',15,'Dating utilizing disequilibrium in the decay of Uranium-238, Uranium-234 and Thorium 230.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Be10',3,'Berylium 10','19',15,'Dating based on the radioactive decay of berylium 10.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('K-Ar',3,'Potassium-Argon','19',15,'Dating based on the radioactive decay of potassium into argon.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Orb-Tuning',21,'Orbital Tuning','19',15,'Dating through the synchronization of events according to Milankovitch cycles, casused by changes in the Earth''s orbit, and their expected effects on climate.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Pa231/U235',3,'U-Series Pa231/U235','19',15,'Dating based on the radioactive decay of uranium-series isotopes protactinium-231 and uranium-235.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Pb210',3,'Lead 210','19',15,'Dating based on the measurement of radioactive isotope lead-210, which is a product of uranium decay.');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Th230/U234',3,'U-Series Th230/U234','19',15,'Dating based on the radioactive decay of uranium-234 to thorium-230, and the degree to which secular equilibrium has been restored between these');
insert into tbl_methods (method_abbrev_or_alt_name, method_group_id, method_name, record_type_id, unit_id, description) values ('Fis-Track',3,'Fission-Track','19',15,'Dating based on the trails left in some glassy minerals as a result of the radioactive decay of uranium.');