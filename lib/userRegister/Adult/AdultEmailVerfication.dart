import 'dart:ffi';

import 'package:bus_season_ticker_users/userRegister/Adult/AdultOTPScreen.dart';
import 'package:bus_season_ticker_users/userRegister/student/OTPVerificationScreen.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class AdultEmailVerfication extends StatefulWidget {
  final String uploadedNICBack;
  final String uploadedNICFront;
  final String name;
  final String intName;
  final String dob;
  final String gender;
  final String telephone;
  final String email;
  final String residence;
  final String address;
  final String photo;
  final String? route;
  final double? charge;
  final Map<String, bool> selectedDays;
  final String? nearestDeport;

  const AdultEmailVerfication({
    super.key,
    required this.uploadedNICBack,
    required this.uploadedNICFront,
    required this.name,
    required this.intName,
    required this.dob,
    required this.gender,
    required this.telephone,
    required this.email,
    required this.residence,
    required this.address,
    required this.photo,
    this.route,
    this.charge,
    required this.selectedDays,
    this.nearestDeport,
  });

  @override
  State<AdultEmailVerfication> createState() => _AdultEmailVerficationState();
}

class _AdultEmailVerficationState extends State<AdultEmailVerfication> {
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
                builder: (context) => AdultOTPScreen(
                    uploadedNICFront: widget.uploadedNICFront,
                    uploadedNICBack: widget.uploadedNICFront,
                    intName: widget.intName,
                    selectedDays: widget.selectedDays,
                    name: widget.name,
                    dob: widget.dob,
                    gender: widget.gender,
                    telephone: widget.telephone,
                    email: widget.email,
                    residence: widget.residence,
                    address: widget.address,
                    photo: widget.photo,
                    route: widget.route,
                    charge: widget.charge,
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
