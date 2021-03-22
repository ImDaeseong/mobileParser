import UIKit
import WebKit
import SwiftSoup

class item1: UIViewController {
    
    //@IBOutlet weak var webview1: WKWebView!

    @IBOutlet weak var webview1: WKWebView!
    override func viewDidLoad() {
        super.viewDidLoad()
      
        webview1.backgroundColor = UIColor.white
        webview1.scrollView.backgroundColor = UIColor.white
        webview1.navigationDelegate = self //WKNavigationDelegate
        loadUrl(sUrl: "http://www.lotto.co.kr/article/list/AC01")
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        
        if webview1 != nil{
            webview1.removeFromSuperview()
            webview1 = nil
        }
    }
    
    func loadUrl(sUrl : String){
        
        webview1.load(URLRequest(url: URL(string:sUrl)!) )
    }

    public func getName(sInput : String) -> String? {
        
        if sInput == "" {
            return nil
        }
        
        if let lastindex = sInput.lastIndex(of: "/") {
            let sResult = String( sInput[ sInput.index( after: lastindex) ..< sInput.endIndex] )
            return sResult
        }
        
        return nil
    }
    
    public func getSubString(sInput : String, sStart : String, sEnd : String) -> String? {
        
        if sInput == "" {
            return nil
        }
        
        var sResult : String = sInput
        
        if let index = sResult.range(of: sStart) {
            
            sResult.removeSubrange(sResult.startIndex ..< index.upperBound)
        }
        
        if let index = sResult.range(of: sEnd) {
            
            sResult.removeSubrange(index.lowerBound ..< sResult.endIndex)
        }
        
        return sResult
    }
}

extension item1 : WKNavigationDelegate{
    
    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        
        //print("페이지 로드 시작")
    }
    
    func webView(_ webView: WKWebView, didReceiveServerRedirectForProvisionalNavigation navigation: WKNavigation!) {
            
        //print("didReceiveServerRedirectForProvisionalNavigation")
    }
       
    func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
            
        //print("didCommit")
    }
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        
        //print("실패")
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        
        //print("완료")
        
        webView.evaluateJavaScript("document.documentElement.innerHTML", completionHandler: { res, error in
            
            if error == nil{
                
                //print(res)
                
                do {
                        
                    let html = res as! String
                    let doc: Document = try SwiftSoup.parse(html)
                    
                    /*
                    let header = try doc.title()
                    let body = doc.body()
                    print(header)
                    print(body)
                    */
  
                    
                    //전체 리스트 확인
                    /*
                    let elarray: Elements = try doc.select(".wnr_cur_list").select("li")
                    for item in elarray {
                        print(try item)
                        //print(try item.text())
                    }
                    */
                    
                    //내가 원하는 부분만 확인
                    /*
                    let elarray: Elements = try doc.select(".wnr_cur_list").select("li")
                    let liarray = try elarray.get(0).select("p")
                    for item in liarray {
                        print(try item)
                    }
                    */
                    
                    /*
                    let elarray: Elements = try doc.select(".wnr_cur_list").select("li")
                    let liarray = try elarray.get(0).select("p")
                    let spanarray = try liarray.select("span")
                    for item in spanarray {
                        print(try item)
                    }
                    */
                    
                    let elarray: Elements = try doc.select(".wnr_cur_list").select("li")
                    let liarray = try elarray.select("p")//try elarray.get(0).select("p")
                    let spanarray = try liarray.select("span")
                    
                    for n in 0..<spanarray.count{
                        
                        //print(n , try spanarray.get(n))
                        
                        if n == 0 {
                            print("회차: ", try spanarray.get(n).text())
                        }else if n == 1 {
                            print("추첨일: ", try spanarray.get(n).text())
                            
                        }else if n == 2 {
                            
                            //print(try spanarray.get(n))
                            
                            /*
                            let imgarray = try spanarray.get(n).select("img")
                            for item in imgarray{
                                print(try item.attr("src"))
                            }
                            */
                            
                            var s1 : String = ""
                            var s2 : String = ""
                            var s3 : String = ""
                            var s4 : String = ""
                            var s5 : String = ""
                            var s6 : String = ""
                            var s7 : String = ""
                            
                            let imgarray = try spanarray.get(n).select("img")
                            for k in 0..<imgarray.count{
                                
                                //print(try imgarray.get(k).attr("src"))
                                
                                let imgpath : String = try imgarray.get(k).attr("src")
                                let result =  try self.getName(sInput: self.getSubString(sInput: imgpath, sStart: "/", sEnd: ".")! )
                                
                                if k == 0 {
                                    
                                    s1 = result!
                                }else if k == 1 {
                                    
                                    s2 = result!
                                }else if k == 2 {
                                    
                                    s3 = result!
                                }else if k == 3 {
                                    
                                    s4 = result!
                                }else if k == 4 {
                                    
                                    s5 = result!
                                }else if k == 5 {
                                    
                                    s6 = result!
                                }else if k == 6 {
                                    
                                    //print(result)
                                }else if k == 7 {
                                    
                                    s7 = result!
                                }
                            }
                            
                            print("당첨번호:" , s1, s2, s3, s4, s5, s6, s7)
                            
                        }else if n == 3 {
                            
                            print("1등 당첨자수: ", try spanarray.get(n).text())
                        }else if n == 4 {
                            
                            print("1등 당첨금액: ", try spanarray.get(n).text())
                        }
                    }
                    
                } catch {
                    print("error")
                }
            }
            
        })
        
    }
    
}
    
