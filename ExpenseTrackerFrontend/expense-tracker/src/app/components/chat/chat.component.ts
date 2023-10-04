import { Component,ChangeDetectorRef  } from '@angular/core';
import { ChatbotService } from '../../services/chatbot_service/chatbot.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Renderer2 } from '@angular/core';
import { SharedService } from 'src/app/services/shared-service/shared.service';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  public userInput: string = "";
  public isChatOpen: boolean = false;
  public messages: { isUser: boolean, text: string }[] = [{isUser: false ,text:"Hello, how can I help you"}];

  constructor(private chatService: ChatbotService,
    private snackBar: MatSnackBar,
    private renderer: Renderer2,
    private sharedService:SharedService,
    private cdRef: ChangeDetectorRef) { }

  
  sendMessage(): void {
    if (!this.userInput.trim()) {
      return;
    }

    this.messages.push({ isUser: true, text: this.userInput });
    this.chatService.sendMessage(this.userInput).subscribe({
      error: e => {
        this.snackBar.open('Error communicating with the chatbot !', 'Close', {duration: 3000});
        console.log(e)
      },
      next: data => {
        const botResponse = data.response.replace(/\n/g, '<br>');
        this.messages.push({ isUser: false, text: botResponse });
        this.userInput = '';

        this.cdRef.detectChanges();
        const chatScrollableArea = this.renderer.selectRootElement('.chat-scrollable-area', true);
        this.renderer.setProperty(chatScrollableArea, 'scrollTop', chatScrollableArea.scrollHeight);
        
      
        if( (data.response.includes("created")) ||
            (data.response.includes("deleted"))){
              this.notifyComponents();
            }
      }
    });
  }

  toggleChat() {
    this.isChatOpen = !this.isChatOpen;
  }

  notifyComponents(){
    this.sharedService.sendMessageEvent();
  }

}
