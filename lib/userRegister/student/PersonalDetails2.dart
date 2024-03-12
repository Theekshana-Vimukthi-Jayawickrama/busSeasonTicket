import 'dart:convert';

import 'package:bus_season_ticker_users/userRegister/student/Document.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class PersonalDetails2 extends StatefulWidget {
  final String name;
  final String intName;
  final String dob;
  final String gender;
  final String telephone;
  final String email;
  final String residence;
  final String address;
  const PersonalDetails2({
    super.key,
    required this.name,
    required this.intName,
    required this.dob,
    required this.gender,
    required this.telephone,
    required this.email,
    required this.residence,
    required this.address,
  });

  @override
  State<PersonalDetails2> createState() => _PersonalDetails2State();
}

class _PersonalDetails2State extends State<PersonalDetails2> {
  final _formKey = GlobalKey<FormState>();

  String? selectedDistrict;
  String _school = '';
  String _indexNumber = '';
  String _guardian = '';
  String _Relation = '';
  String _occupation = '';
  String _contactNumber = '';
  List<String> districts = [];

  @override
  void initState() {
    super.initState();
    getDistrictList();
  }

  Future<void> getDistrictList() async {
    var url =
        Uri.parse("http://192.168.43.220:8080/api/v1/admin/getAllSchDistrict");
    var response = await http.get(url);
    try {
      if (response.statusCode == 200) {
        List<dynamic> jsonResponse = jsonDecode(response.body);
        setState(() {
          districts = jsonResponse.cast<String>();
        });
      } else {
        print('HTTP Error: ${response.statusCode}');
      }
    } catch (e) {
      print('Error occurred: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: const Text(
            "Personal Deails",
            style: TextStyle(color: Color(0xFFFFFFFF)),
          ),
          backgroundColor: Color(0xFF881C34),
        ),
        body: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              Container(
                  padding: const EdgeInsets.only(left: 25, right: 25),
                  child: Column(children: <Widget>[
                    const SizedBox(
                      height: 20,
                    ),
                    const Text(
                      "This section contains 03 sections namely student’s details, school details and guardian details.",
                      textAlign: TextAlign.justify,
                      style: TextStyle(color: Color(0xFF881C34)),
                    ),
                    const SizedBox(
                      height: 20,
                    ),
                    Container(
                      padding: const EdgeInsets.all(5),
                      color: const Color(0xFF262E7B),
                      width: double.infinity,
                      child: const Text("school Deatils",
                          style: TextStyle(color: Color(0xFFFFFFFF))),
                    ),
                    Form(
                        key: _formKey,
                        child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              TextFormField(
                                decoration: const InputDecoration(
                                    labelText: 'Address of the School'),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your school';
                                  }
                                  return null;
                                },
                                onSaved: (value) {
                                  _school = value ??
                                      ''; // It returns the expression on its left if it's not null, otherwise, it returns the expression on its right
                                },
                              ),
                              DropdownButtonFormField<String>(
                                value: selectedDistrict,
                                hint: Text('Select District'),
                                onChanged: (String? value) {
                                  setState(() {
                                    selectedDistrict = value;
                                  });
                                },
                                items: districts.map((String district) {
                                  return DropdownMenuItem<String>(
                                    value: district,
                                    child: Text(district),
                                  );
                                }).toList(),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please select your district';
                                  }
                                },
                              ),
                              SizedBox(height: 16.0),
                              TextFormField(
                                decoration:
                                    InputDecoration(labelText: 'Index Number'),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your indexnumber';
                                  }
                                  return null;
                                },
                                onSaved: (value) {
                                  _indexNumber = value ?? '';
                                },
                              ),
                              SizedBox(height: 20.0),
                              Container(
                                padding: const EdgeInsets.all(5),
                                color: const Color(0xFF262E7B),
                                width: double.infinity,
                                child: const Text("Guardian Deatils",
                                    style: TextStyle(color: Color(0xFFFFFFFF))),
                              ),
                              TextFormField(
                                decoration: const InputDecoration(
                                    labelText: 'Name of The Guardian'),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your guardian name';
                                  }
                                  return null;
                                },
                                onSaved: (value) {
                                  _guardian = value ?? '';
                                },
                              ),
                              const Padding(
                                padding: EdgeInsets.only(top: 20, bottom: 0),
                                child: Text(
                                  'Relation',
                                  style: TextStyle(fontSize: 15),
                                ),
                              ),
                              Row(
                                children: [
                                  Radio(
                                    value: 'Mother',
                                    groupValue: _Relation,
                                    onChanged: (value) {
                                      setState(() {
                                        _Relation = value.toString();
                                      });
                                    },
                                  ),
                                  Text('Mother'),
                                  Radio(
                                    value: 'Father',
                                    groupValue: _Relation,
                                    onChanged: (value) {
                                      setState(() {
                                        _Relation = value.toString();
                                      });
                                    },
                                  ),
                                  Text('Father'),
                                  Radio(
                                    value: 'Guardian',
                                    groupValue: _Relation,
                                    onChanged: (value) {
                                      setState(() {
                                        _Relation = value.toString();
                                      });
                                    },
                                  ),
                                  Text('Guardian'),
                                ],
                              ),
                              TextFormField(
                                decoration: const InputDecoration(
                                    labelText: 'Occupation'),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your guardian occupation';
                                  }
                                  return null;
                                },
                                onSaved: (value) {
                                  _occupation = value ?? '';
                                },
                              ),
                              TextFormField(
                                decoration: const InputDecoration(
                                    labelText: 'Contact Number'),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your guardian contact number';
                                  }
                                  // Remove any non-digit characters from the input
                                  String cleanedPhoneNumber =
                                      value.replaceAll(RegExp(r'\D'), '');

                                  if (cleanedPhoneNumber.length != 10) {
                                    return 'Please enter a valid 10-digit telephone number';
                                  }
                                  return null;
                                },
                                onSaved: (value) {
                                  _contactNumber = value ?? '';
                                },
                              ),
                              const SizedBox(
                                height: 20,
                              ),
                              Row(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                children: [
                                  Container(
                                    padding: EdgeInsets.only(bottom: 0),
                                    height: 44,
                                    width: 120,
                                    decoration: const BoxDecoration(
                                        color: Color(0xFFFFFFFF),
                                        borderRadius: BorderRadius.all(
                                            Radius.circular(20))),
                                    child: ElevatedButton(
                                      onPressed: () {
                                        Navigator.pop(context);
                                      },
                                      style: ElevatedButton.styleFrom(
                                        backgroundColor: const Color(
                                            0xFF881C34), // Set the background color to blue
                                        elevation: 5,
                                        shadowColor: Colors.blueAccent,
                                      ),
                                      child: const Text(
                                        "Back",
                                        textAlign: TextAlign.center,
                                        style: TextStyle(
                                            color: Color(0xFFFFFFFF),
                                            fontSize: 20.0,
                                            fontWeight: FontWeight.bold),
                                      ),
                                    ),
                                  ),
                                  Container(
                                    padding: const EdgeInsets.only(bottom: 0),
                                    height: 44,
                                    width: 120,
                                    decoration: const BoxDecoration(
                                        color: Color(0xFFFFFFFF),
                                        borderRadius: BorderRadius.all(
                                            Radius.circular(20))),
                                    child: ElevatedButton(
                                      onPressed: () {
                                        if (_formKey.currentState?.validate() ??
                                            false) {
                                          // All fields are valid, save the form
                                          _formKey.currentState?.save();

                                          // Navigate to the next page and pass form data as arguments
                                          Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                              builder: (context) => Document(
                                                  intName: widget.intName,
                                                  name: widget.name,
                                                  dob: widget.dob.trim(),
                                                  gender: widget.gender,
                                                  telephone:
                                                      widget.telephone.trim(),
                                                  email: widget.email,
                                                  residence: widget.residence,
                                                  address: widget.address,
                                                  selectedDistrict:
                                                      selectedDistrict
                                                          .toString()
                                                          .trim()
                                                          .toLowerCase(),
                                                  school: _school,
                                                  indexNumber:
                                                      _indexNumber.trim(),
                                                  guardian: _guardian,
                                                  Relation: _Relation,
                                                  occupation: _Relation,
                                                  contactNumber:
                                                      _contactNumber.trim()),
                                            ),
                                          );
                                        }
                                      },
                                      style: ElevatedButton.styleFrom(
                                        backgroundColor: const Color(
                                            0xFF881C34), // Set the background color to blue
                                        elevation: 5,
                                        shadowColor: Colors.blueAccent,
                                      ),
                                      child: const Text(
                                        "Next",
                                        textAlign: TextAlign.center,
                                        style: TextStyle(
                                            color: Color(0xFFFFFFFF),
                                            fontSize: 20.0,
                                            fontWeight: FontWeight.bold),
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ]))
                  ]))
            ],
          ),
        ),
      ),
    );
  }
}
