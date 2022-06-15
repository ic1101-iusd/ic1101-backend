actor class Protocol () = this {

 public type Position = {
    id: Int;
    address: Text;
    collateral: Float;
    usb: Float;
  };

  public query func getPositions() : async [Position] {
  var test : Position = {
         id = 1;
        address = "test";
        collateral = 1.77;
        usb = 1;
    };
  return [test];
  };


  table var lig : Int = 0;

  public func liquidate() : async () {
       lig += 1;
  };


  public query func getPrice() : async Float {
    return 30000;
  };
};
