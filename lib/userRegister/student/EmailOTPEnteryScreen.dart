import 'dart:ffi';

import 'package:bus_season_ticker_users/userRegister/student/OTPVerificationScreen.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class EmailEnteryScreen extends StatefulWidget {
  final String BirthFilePath;
  final String ApprovalFilePath;
  final String name;
  final String intName;
  final String dob;
  final String gender;
  final String telephone;
  final String email;
  final String residence;
  final String address;
  final String school;
  final String indexNumber;
  final String guardian;
  final String Relation;
  final String occupation;
  final String contactNumber;
  final String? selectedDistrict;
  final String? route;
  final double? charge;
  final String nearestDeport;
  final String photo;

  const EmailEnteryScreen(
      {super.key,
      required this.BirthFilePath,
      required this.ApprovalFilePath,
      required this.name,
      required this.intName,
      required this.dob,
      required this.gender,
      required this.telephone,
      required this.email,
      required this.residence,
      required this.address,
      required this.school,
      required this.indexNumber,
      required this.guardian,
      required this.Relation,
      required this.occupation,
      required this.contactNumber,
      required this.selectedDistrict,
      required this.route,
      required this.charge,
      required this.nearestDeport,
      required this.photo});

  @override
  State<EmailEnteryScreen> createState() => _EmailEnteryScreenState();
}

class _EmailEnteryScreenState extends State<EmailEnteryScreen> {
  final TextEditingController _emailController = TextEditingController();
  final _formKey = GlobalKey<FormState>();

  Future<void> sendOTP(
    userEmail,
    BuildContext context,
  ) async {
    String email = widget.email.trim().toLowerCase();
    String typedEmail = userEmail
        .toString()
        .trim()
        .toLowerCase(); //remove leading and trailing whitespace (spaces, tabs, newlines, etc.) from a string.

    if (typedEmail == email) {
      var url = Uri.parse('http://192.168.43.220:8080/api/v1/auth/sendOTP');
      var response = (await http.post(url, body: {'email': typedEmail}));
      String responseBody = response.body;

      if (response.statusCode == 200) {
        // ignore: use_build_context_synchronously
        Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => OTPVerificationScreen(
                    BirthFilePath: widget.BirthFilePath,
                    ApprovalFilePath: widget.ApprovalFilePath,
                    intName: widget.intName,
                    name: widget.name,
                    dob: widget.dob,
                    gender: widget.gender,
                    telephone: widget.telephone,
                    email: widget.email,
                    residence: widget.residence,
                    address: widget.address,
                    selectedDistrict: widget.selectedDistrict,
                    school: widget.school,
                    indexNumber: widget.indexNumber,
                    guardian: widget.guardian,
                    Relation: widget.Relation,
                    occupation: widget.occupation,
                    contactNumber: widget.contactNumber,
                    route: widget.route,
                    charge: widget.charge,
                    photo: widget.photo,
                    nearestDeport: widget.nearestDeport)));
      } else {
        String message = responseBody as String;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(message),
            duration: Duration(seconds: 3),
          ),
        );
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text(
              "Please, Enter your email that previous you entered at personal details page."),
          duration: Duration(seconds: 6),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Color(0xFF881C34),
          title: const Text('Enter Email',
              style: TextStyle(color: Color(0xFFFFFFFF))),
          // automaticallyImplyLeading: false,
        ),
        body: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                SizedBox(
                  height: 200,
                ),
                Form(
                  key: _formKey,
                  child: Column(
                    children: [
                      TextField(
                        controller: _emailController,
                        keyboardType: TextInputType.emailAddress,
                        decoration: const InputDecoration(
                          labelText: 'Enter Email',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: () {
                          if (_formKey.currentState?.validate() ?? false) {
                            _formKey.currentState?.save();
                            sendOTP(_emailController.text, context);
                          }
                        },
                        child: Text('Send OTP'),
                      ),
                    ],
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
