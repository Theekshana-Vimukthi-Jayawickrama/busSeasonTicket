import 'package:bus_season_ticker_users/userRegister/student/SuccessfulVerificationScreen.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:async';

class OTPVerificationScreen extends StatefulWidget {
  final String email;
  final String BirthFilePath;
  final String ApprovalFilePath;
  final String name;
  final String intName;
  final String dob;
  final String gender;
  final String telephone;
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
  final String photo;
  final String nearestDeport;

  const OTPVerificationScreen(
      {super.key,
      required this.email,
      required this.BirthFilePath,
      required this.ApprovalFilePath,
      required this.name,
      required this.intName,
      required this.dob,
      required this.gender,
      required this.telephone,
      required this.residence,
      required this.address,
      required this.school,
      required this.indexNumber,
      required this.guardian,
      required this.Relation,
      required this.occupation,
      required this.contactNumber,
      this.selectedDistrict,
      this.route,
      this.charge,
      required this.photo,
      required this.nearestDeport});

  @override
  State<OTPVerificationScreen> createState() => _OTPVerificationScreenState();
}

class _OTPVerificationScreenState extends State<OTPVerificationScreen> {
  final TextEditingController _otpController = TextEditingController();
  bool _isResendEnabled = true;
  int _resendCountdown = 120; // Countdown in seconds

  Timer? _resendTimer;

  @override
  void dispose() {
    _resendTimer?.cancel();
    super.dispose();
  }

  Future<void> fetchOTP(String otp, BuildContext context) async {
    String email = widget.email.trim().toString().toLowerCase();
    var url = Uri.parse('http://192.168.43.220:8080/api/v1/auth/verifyOTP');
    var response = await http.post(
      url,
      body: {
        'email': email,
        'otp': otp,
      },
    );

    try {
      if (response.statusCode == 200) {
        // ignore: use_build_context_synchronously
        Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => SuccessfulVerificationScreen(
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
        print('Request successful');
      } else {
        String message = response.body;
        // ignore: use_build_context_synchronously
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(message),
            duration: Duration(seconds: 2),
          ),
        );
        // Request failed
        print('Failed with status ${response.statusCode}');
      }
    } catch (e) {
      // An error occurred
      print('Error: $e');
    }
  }

  Future<void> _resendOTP() async {
    String sendEmail = widget.email.trim();
    var url = Uri.parse('http://192.168.43.220:8080/api/v1/auth/reSendOTP');
    var response = await http.post(
      url,
      body: {'email': sendEmail},
    );

    try {
      if (response.statusCode == 200) {
        _disableResendButtonForDuration();
        // ignore: use_build_context_synchronously
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('OTP Resent to ${sendEmail}'),
            duration: Duration(seconds: 3),
          ),
        );
      }
    } catch (e) {
      print('Error: $e');
    }
  }

  void handleResendButtonPress() {
    if (_isResendEnabled) {
      _resendOTP();
    }
  }

  void _disableResendButtonForDuration() {
    setState(() {
      _isResendEnabled = false;
      _resendCountdown = 120;
      _resendTimer = Timer.periodic(const Duration(seconds: 1), (timer) {
        if (_resendCountdown == 0) {
          _resendTimer?.cancel(); // Cancel the timer when countdown reaches 0
          setState(() {
            _isResendEnabled = true; // Enable resend button after 2 minutes
          });
        } else {
          setState(() {
            _resendCountdown--;
          });
        }
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Color(0xFF881C34),
          title: const Text('OTP Verification',
              style: TextStyle(color: Color(0xFFFFFFFF))),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text(
                'Enter OTP sent to ',
                style: TextStyle(fontSize: 18.0),
              ),
              SizedBox(height: 20),
              Column(
                children: [
                  TextFormField(
                    controller: _otpController,
                    keyboardType: TextInputType.number,
                    textAlign: TextAlign.center,
                    maxLength: 6,
                    onChanged: (value) {
                      if (value.length == 6) {
                        fetchOTP(value, context);
                      }
                    },
                    decoration: const InputDecoration(
                      counterText: '',
                      border: OutlineInputBorder(),
                    ),
                  ),
                  SizedBox(height: 20),
                  ElevatedButton(
                    onPressed:
                        _isResendEnabled ? handleResendButtonPress : null,
                    child: Text('Resend OTP'),
                  ),
                  SizedBox(height: 10),
                  _isResendEnabled
                      ? Container()
                      : Text(
                          'Resend disabled for ${_resendCountdown ~/ 60}:${(_resendCountdown % 60).toString().padLeft(2, '0')}',
                          style: TextStyle(fontSize: 16),
                        ),
                ],
              )
            ],
          ),
        ),
      ),
    );
  }
}
