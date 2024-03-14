import 'package:bus_season_ticker_users/userRegister/student/SetUsername&Password.dart';
import 'package:flutter/material.dart';

class SuccessfulVerificationScreen extends StatefulWidget {
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
  const SuccessfulVerificationScreen(
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
  State<SuccessfulVerificationScreen> createState() =>
      _SuccessfulVerificationScreenState();
}

class _SuccessfulVerificationScreenState
    extends State<SuccessfulVerificationScreen> {
  @override
  void initState() {
    super.initState();
    Future.delayed(const Duration(seconds: 3), () {
      Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => setUsernameAndPassword(
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
    });
  }

  @override
  Widget build(BuildContext context) {
    //  Future.delayed for navigation after 3 seconds
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          title: const Center(
            child: Text(
              "Successful Verification",
              style: TextStyle(color: Color(0xFFFFFFFF)),
            ),
          ),
          backgroundColor: Color(0xFF881C34),
        ),
        body: Stack(
          children: [
            const Align(
              alignment: Alignment.center,
              child: Padding(
                padding: EdgeInsets.only(top: 5),
                child: Text(
                  'Verification Successful!',
                  style: TextStyle(fontSize: 18.0),
                ),
              ),
            ),
            Align(
                alignment: Alignment.topCenter,
                child: Padding(
                  padding: const EdgeInsets.only(top: 280, bottom: 50),
                  child: Image.asset("assets/success.png"),
                )),
          ],
        ),
      ),
    );
  }
}
