package org.brightblock.mam.ethereum.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple9;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class ArtMarket extends Contract {
    private static final String BINARY = "608060405260001960025560001960055534801561001c57600080fd5b5060008054600160a060020a03191633179055610fe48061003e6000396000f3006080604052600436106100f05763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663180dc8f781146100f55780631f9cc9af14610194578063236ed8f3146101ac5780633fc90920146101c4578063571a26a01461021d5780637aacf03c1461028d5780638d2c4488146102b45780638da5cb5b146102d55780638df6d6e6146103065780639979ef451461031e578063b7dc3b1814610329578063bbe156271461037a578063bfb231d214610410578063d79875eb14610524578063e07bb1a71461053f578063ef78ad9a14610554578063fcc08cd414610580575b600080fd5b34801561010157600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261019294369492936024939284019190819084018382808284375050604080516020601f818a01358b0180359182018390048302840183018552818452989b8a359b909a90999401975091955091820193509150819084018382808284375094975061059b9650505050505050565b005b3480156101a057600080fd5b5061019260043561067f565b3480156101b857600080fd5b5061019260043561072f565b3480156101d057600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526101929436949293602493928401919081908401838280828437509497506108bd9650505050505050565b34801561022957600080fd5b506102356004356108e1565b60408051998a5260208a01989098528888019690965260608801949094526080870192909252600160a060020a0390811660a087015260c08601919091521660e0840152151561010083015251908190036101200190f35b34801561029957600080fd5b506102a261093a565b60408051918252519081900360200190f35b3480156102c057600080fd5b50610192600435602435604435606435610940565b3480156102e157600080fd5b506102ea610a24565b60408051600160a060020a039092168252519081900360200190f35b34801561031257600080fd5b506102a2600435610a33565b610192600435610a53565b60408051602060046024803582810135601f8101859004850286018501909652858552610192958335953695604494919390910191908190840183828082843750949750610b639650505050505050565b34801561038657600080fd5b5061039b600160a060020a0360043516610c64565b6040805160208082528351818301528351919283929083019185019080838360005b838110156103d55781810151838201526020016103bd565b50505050905090810190601f1680156104025780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561041c57600080fd5b50610428600435610cff565b60408051908101859052606081018490526080810183905281151560a082015260c08082528751908201528651819060208083019160e08401918b019080838360005b8381101561048357818101518382015260200161046b565b50505050905090810190601f1680156104b05780820380516001836020036101000a031916815260200191505b5083810382528851815288516020918201918a019080838360005b838110156104e35781810151838201526020016104cb565b50505050905090810190601f1680156105105780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b34801561053057600080fd5b50610192600435602435610e55565b34801561054b57600080fd5b506102a2610ebf565b34801561056057600080fd5b5061056c600435610ec5565b604080519115158252519081900360200190f35b34801561058c57600080fd5b506102ea600435602435610eda565b60008281526003602052604090205460ff16156105b757600080fd5b6002805460019081019182905560009182526020908152604090912084516105e192860190610f1d565b5060028054600090815260016020818152604080842085018790559354835292909120835161061893919092019190840190610f1d565b50506002805460009081526001602081815260408084206003908101859055945484528084208480526004018252808420805473ffffffffffffffffffffffffffffffffffffffff1916331790559483529290925291909120805460ff1916909117905550565b60008181526004602052604090206009015460ff1680156106ba5750600081815260046020526040902060070154600160a060020a03163314155b15156106c557600080fd5b6000818152600460209081526040808320338085526008909101909252808320549051919281156108fc029290818181858888f1935050505015801561070f573d6000803e3d6000fd5b506000908152600460209081526040808320338452600801909152812055565b60008181526004602052604090206009015460ff161580156107605750600081815260046020526040812060010154115b151561076b57600080fd5b60008181526004602052604090206002810154600190910154420311156108ba5760008181526004602052604081206006015411156108815760008181526004602081815260408084208054855260018352818520600381015486528401835281852054868652939092526006909101549051600160a060020a039092169281156108fc029290818181858888f1935050505015801561080f573d6000803e3d6000fd5b5060008181526004602081815260408084208054855260018084528286206003908101805483019055600783015492548752908452828620908101548652909301909152909120805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039092169190911790555b6000818152600460209081526040808320600981018054600160ff19918216811790925591548552909252909120600601805490911690555b50565b33600090815260066020908152604090912082516108dd92840190610f1d565b5050565b60046020819052600091825260409091208054600182015460028301546003840154948401546005850154600686015460078701546009909701549597949693959293600160a060020a03928316939192169060ff1689565b60055481565b600084815260016020908152604080832060038101548452600401909152902054600160a060020a0316331480156109875750600084815260016020526040902060050154155b151561099257600080fd5b60058054600190810180835560009081526004602081815260408084208a905585548452808420600201989098558454835287832060030196909655835482528682200193909355815483528483208201805473ffffffffffffffffffffffffffffffffffffffff1916331790559054825283822042908201559381529083905220600601805460ff19169091179055565b600054600160a060020a031681565b600090815260046020908152604080832033845260080190915290205490565b60008181526004602052604090206009015460ff16158015610a845750600081815260046020526040812060010154115b8015610ab45750600081815260046020908152604080832060038101543385526008909101909252909120543401115b8015610aff575060008181526004602081905260409091206006810154910154610ade9190610f04565b60008281526004602090815260408083203384526008019091529020543401115b1515610b0a57600080fd5b600090815260046020908152604080832060078101805473ffffffffffffffffffffffffffffffffffffffff19163390811790915584526008810190925290912080543490810160069093019290925580549091019055565b600082815260016020526040812060050154118015610b92575060008281526001602052604090206005015434145b156108dd57600082815260016020908152604080832060038101548452600401909152808220549051600160a060020a03909116913480156108fc02929091818181858888f19350505050158015610bee573d6000803e3d6000fd5b5060008281526001602081815260408084206003810180548501908190558552600481018352908420805473ffffffffffffffffffffffffffffffffffffffff191633179055928590528181528351610c4e939092019190840190610f1d565b5050600090815260016020526040812060050155565b60066020908152600091825260409182902080548351601f600260001961010060018616150201909316929092049182018490048402810184019094528084529091830182828015610cf75780601f10610ccc57610100808354040283529160200191610cf7565b820191906000526020600020905b815481529060010190602001808311610cda57829003601f168201915b505050505081565b60016020818152600092835260409283902080548451600294821615610100026000190190911693909304601f8101839004830284018301909452838352928391830182828015610d915780601f10610d6657610100808354040283529160200191610d91565b820191906000526020600020905b815481529060010190602001808311610d7457829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610e2f5780601f10610e0457610100808354040283529160200191610e2f565b820191906000526020600020905b815481529060010190602001808311610e1257829003601f168201915b505050506002830154600384015460058501546006909501549394919390925060ff1686565b600082815260016020908152604080832060038101548452600401909152902054600160a060020a031633148015610e9f575060008281526001602052604090206006015460ff16155b1515610eaa57600080fd5b60009182526001602052604090912060050155565b60025481565b60036020526000908152604090205460ff1681565b600091825260016020908152604080842092845260049092019052902054600160a060020a031690565b600082820183811015610f1657600080fd5b9392505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610f5e57805160ff1916838001178555610f8b565b82800160010185558215610f8b579182015b82811115610f8b578251825591602001919060010190610f70565b50610f97929150610f9b565b5090565b610fb591905b80821115610f975760008155600101610fa1565b905600a165627a7a7230582035991c53ceadc20424b2596f0125e137f8fd6851c48a347d29e0a7283dbc8e9f0029";

    public static final String FUNC_ADDITEM = "addItem";

    public static final String FUNC_RECLAIMESCROW = "reclaimEscrow";

    public static final String FUNC_CLOSEAUCTION = "closeAuction";

    public static final String FUNC_REGISTERPROFILE = "registerProfile";

    public static final String FUNC_AUCTIONS = "auctions";

    public static final String FUNC_AUCTIONINDEX = "auctionIndex";

    public static final String FUNC_STARTAUCTION = "startAuction";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_GETMYBID = "getMyBid";

    public static final String FUNC_PLACEBID = "placeBid";

    public static final String FUNC_BUY = "buy";

    public static final String FUNC_PROFILES = "profiles";

    public static final String FUNC_ITEMS = "items";

    public static final String FUNC_SELL = "sell";

    public static final String FUNC_ITEMINDEX = "itemIndex";

    public static final String FUNC_ITEMEXISTS = "itemExists";

    public static final String FUNC_GETITEMOWNER = "getItemOwner";

    protected ArtMarket(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ArtMarket(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> addItem(String title, byte[] hash, String blockstackUrl) {
        final Function function = new Function(
                FUNC_ADDITEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(title), 
                new org.web3j.abi.datatypes.generated.Bytes32(hash), 
                new org.web3j.abi.datatypes.Utf8String(blockstackUrl)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> reclaimEscrow(BigInteger auctionID) {
        final Function function = new Function(
                FUNC_RECLAIMESCROW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(auctionID)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> closeAuction(BigInteger auctionID) {
        final Function function = new Function(
                FUNC_CLOSEAUCTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(auctionID)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> registerProfile(String url) {
        final Function function = new Function(
                FUNC_REGISTERPROFILE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(url)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple9<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, String, Boolean>> auctions(BigInteger param0) {
        final Function function = new Function(FUNC_AUCTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple9<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, String, Boolean>>(
                new Callable<Tuple9<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, String, Boolean>>() {
                    @Override
                    public Tuple9<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple9<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, String, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (String) results.get(7).getValue(), 
                                (Boolean) results.get(8).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> auctionIndex() {
        final Function function = new Function(FUNC_AUCTIONINDEX, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> startAuction(BigInteger itemID, BigInteger duration, BigInteger reserve, BigInteger increment) {
        final Function function = new Function(
                FUNC_STARTAUCTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(itemID), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.generated.Uint256(reserve), 
                new org.web3j.abi.datatypes.generated.Uint256(increment)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getMyBid(BigInteger auctionID) {
        final Function function = new Function(FUNC_GETMYBID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(auctionID)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> placeBid(BigInteger auctionID, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_PLACEBID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(auctionID)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> buy(BigInteger itemID, String blockstackUrl, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BUY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(itemID), 
                new org.web3j.abi.datatypes.Utf8String(blockstackUrl)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<String> profiles(String param0) {
        final Function function = new Function(FUNC_PROFILES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean>> items(BigInteger param0) {
        final Function function = new Function(FUNC_ITEMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean>>(
                new Callable<Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, byte[], BigInteger, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> sell(BigInteger itemID, BigInteger price) {
        final Function function = new Function(
                FUNC_SELL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(itemID), 
                new org.web3j.abi.datatypes.generated.Uint256(price)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> itemIndex() {
        final Function function = new Function(FUNC_ITEMINDEX, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> itemExists(byte[] param0) {
        final Function function = new Function(FUNC_ITEMEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> getItemOwner(BigInteger itemID, BigInteger ownerIndex) {
        final Function function = new Function(FUNC_GETITEMOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(itemID), 
                new org.web3j.abi.datatypes.generated.Uint256(ownerIndex)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<ArtMarket> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ArtMarket.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ArtMarket> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ArtMarket.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static ArtMarket load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArtMarket(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ArtMarket load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArtMarket(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
